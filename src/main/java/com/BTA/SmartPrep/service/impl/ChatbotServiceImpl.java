package com.BTA.SmartPrep.service.impl;
import com.BTA.SmartPrep.domain.dto.chatbot.ChatbotEvaluateRequestDto;
import com.BTA.SmartPrep.domain.dto.chatbot.ChatbotEvaluateResponseDto;
import com.BTA.SmartPrep.domain.entity.Proficiency;
import com.BTA.SmartPrep.domain.entity.SolutionRating;
import com.BTA.SmartPrep.domain.entity.Submission;
import com.BTA.SmartPrep.domain.entity.User;
import com.BTA.SmartPrep.repository.ProficiencyRepository;
import com.BTA.SmartPrep.repository.SubmissionRepository;
import com.BTA.SmartPrep.repository.UserRepository;
import com.BTA.SmartPrep.service.ChatbotService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=";

    private final ProficiencyRepository proficiencyRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final ObjectMapper objectMapper;

    public ChatbotServiceImpl(ProficiencyRepository proficiencyRepository,
                              UserRepository userRepository,
                              SubmissionRepository submissionRepository) {
        this.proficiencyRepository = proficiencyRepository;
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ChatbotEvaluateResponseDto evaluate(ChatbotEvaluateRequestDto request) {
        String prompt = buildPrompt(request.question(), request.difficulty(), request.answer());

        String rawText;
        try {
            rawText = callGemini(prompt);
        } catch (RuntimeException e) {
            if ("RATE_LIMITED".equals(e.getMessage())) {
                return new ChatbotEvaluateResponseDto(0, "ERROR",
                        "Rate limit reached. Please wait a few seconds and try again.");
            }
            throw e;
        }

        // ... rest of the method stays exactly the same
        // 2. Parse the 4 criteria scores from Gemini's JSON response
        int relevance = 0, correctness = 0, completeness = 0, clarity = 0;
        String feedback = "No feedback available.";

        try {
            // Gemini sometimes wraps JSON in markdown fences - strip them
            String cleaned = rawText.trim()
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```\\s*", "")
                    .trim();

            JsonNode node = objectMapper.readTree(cleaned);
            relevance    = node.path("relevance").asInt(0);
            correctness  = node.path("correctness").asInt(0);
            completeness = node.path("completeness").asInt(0);
            clarity      = node.path("clarity").asInt(0);
            feedback     = node.path("feedback").asText("No feedback available.");

        } catch (Exception e) {
            // If parsing fails, default to a low score so the demo doesn't crash
            System.err.println("Failed to parse Gemini response: " + e.getMessage());
            System.err.println("Raw response was: " + rawText);
        }

        // 3. Total score out of 100 (each criterion is out of 25)
        int totalScore = relevance + correctness + completeness + clarity;
        totalScore = Math.min(100, Math.max(0, totalScore)); // clamp 0-100

        // 4. Determine rating
        SolutionRating rating;
        if (totalScore >= 80) {
            rating = SolutionRating.GREEN;
        } else if (totalScore >= 60) {
            rating = SolutionRating.YELLOW;
        } else {
            rating = SolutionRating.RED;
        }

        // 5. Update proficiency score based on rating
        updateProficiency(request.userId(), request.categoryId(), rating);

        // 6. Save submission record
        saveSubmission(request.userId(), request.answer(), rating);

        return new ChatbotEvaluateResponseDto(totalScore, rating.name(), feedback);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private String buildPrompt(String question, String difficulty, String answer) {
        return String.format("""
                You are a technical interview evaluator for software engineering candidates.
                The candidate was asked the following %s difficulty question:
                
                Question: "%s"
                
                Their answer: "%s"
                
                Score the answer on each of these four criteria, each out of 25 points:
                1. relevance     - Does the answer address the actual question asked?
                2. correctness   - Is the technical content accurate and correct?
                3. completeness  - Is the explanation thorough enough?
                4. clarity       - Is the answer clear and well-communicated?
                
                Reply ONLY with this exact JSON and nothing else - no markdown, no extra text:
                {"relevance":<score>,"correctness":<score>,"completeness":<score>,"clarity":<score>,"feedback":"<one concise sentence of feedback>"}
                """, difficulty, question, answer);
    }

    private String callGemini(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format("""
        {
          "contents": [
            {
              "parts": [
                { "text": %s }
              ]
            }
          ]
        }
        """, objectMapper.valueToTree(prompt).toString());

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        int maxRetries = 3;

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(
                        GEMINI_URL + geminiApiKey,
                        entity,
                        String.class
                );

                JsonNode root = objectMapper.readTree(response.getBody());
                return root
                        .path("candidates").get(0)
                        .path("content")
                        .path("parts").get(0)
                        .path("text").asText();

            } catch (org.springframework.web.client.HttpServerErrorException.ServiceUnavailable e) {
                // 🔥 503 retry
                try {
                    Thread.sleep(1000 * (attempt + 1)); // backoff
                } catch (InterruptedException ignored) {}
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                if (e.getStatusCode().value() == 429) {
                    throw new RuntimeException("RATE_LIMITED");
                }
                throw new RuntimeException("Gemini API error: " + e.getStatusCode(), e);
            } catch (Exception e) {
                // final fallback
                if (attempt == maxRetries - 1) {
                    return "{\"relevance\":10,\"correctness\":10,\"completeness\":10,\"clarity\":10,\"feedback\":\"AI temporarily unavailable. Try again.\"}";
                }
            }
        }

        // fallback safety
        return "{\"relevance\":10,\"correctness\":10,\"completeness\":10,\"clarity\":10,\"feedback\":\"AI temporarily unavailable.\"}";
    }

    private void updateProficiency(String userId, int categoryId, SolutionRating rating) {
        Optional<Proficiency> optional = proficiencyRepository
                .findByIdUserIdAndIdCategoryId(userId, categoryId);

        if (optional.isPresent()) {
            Proficiency proficiency = optional.get();
            int current = proficiency.getProficiency();

            int updated = switch (rating) {
                case GREEN  -> Math.min(100, current + 10);
                case YELLOW -> current; // no change
                case RED    -> Math.max(1, current - 10);
            };

            proficiency.setProficiency(updated);
            proficiencyRepository.save(proficiency);
        }
    }

    private void saveSubmission(String userId, String answer, SolutionRating rating) {
        try {
            Optional<User> userOptional = userRepository.findById(UUID.fromString(userId));
            if (userOptional.isPresent()) {
                Submission submission = new Submission();
                submission.setUser(userOptional.get());
                submission.setAnswer(answer);
                submission.setSolutionRating(rating);
                submission.setProblem(null); // chatbot has no associated problem
                submissionRepository.save(submission);
            }
        } catch (Exception e) {
            // Non-critical - don't let a failed save break the response
            System.err.println("Could not save chatbot submission: " + e.getMessage());
        }
    }
}