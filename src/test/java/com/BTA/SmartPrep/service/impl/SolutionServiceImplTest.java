package com.BTA.SmartPrep.service.impl;

import com.BTA.SmartPrep.domain.dto.problem.SolutionSubmissionDto;
import com.BTA.SmartPrep.domain.entity.*;
import com.BTA.SmartPrep.repository.*;
import com.BTA.SmartPrep.service.ProfficiencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolutionServiceImplTest {
    @Mock private ProblemRepository problemRepository;
    @Mock private TestCaseRepository testCaseRepository;
    @Mock private ProfficiencyService profficiencyService;
    @Mock private UserRepository userRepository;
    @Mock private SubmissionRepository submissionRepository;

    @Test
    void gradingPersistsCodingSubmissionWithOwnerProblemSourceAndGrade() {
        long problemId = 101L;
        String userId = "1df278cf-cf43-4bf7-9c34-dc9a9ded6ead";
        String source = "public class Solution { public boolean containsDuplicate(int[] nums) { return nums.length > 1 && nums[0] == nums[1]; } }";
        Problem problem = new Problem(problemId, "Contains Duplicate", "", "[1,1]",
                ProblemDifficulty.EASY, 1, source, "", "containsDuplicate", "boolean", "int[]", "true");
        TestCase testCase = new TestCase("case-1", (int) problemId, "[1,1]", "true", 0);
        User user = new User(userId, "Demo", "demo@example.test", "hash");
        when(problemRepository.findById(problemId)).thenReturn(Optional.of(problem));
        when(testCaseRepository.findAllByProblemId(problemId)).thenReturn(List.of(testCase));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        SolutionServiceImpl service = new SolutionServiceImpl(
                problemRepository, testCaseRepository, new ObjectMapper(), profficiencyService,
                userRepository, submissionRepository);

        SolutionSubmissionDto result = service.solutionGrade(source, problemId, userId, 1);

        ArgumentCaptor<Submission> saved = ArgumentCaptor.forClass(Submission.class);
        verify(submissionRepository).save(saved.capture());
        assertEquals(user, saved.getValue().getUser());
        assertEquals(problem, saved.getValue().getProblem());
        assertEquals(source, saved.getValue().getAnswer());
        assertEquals(SolutionRating.valueOf(result.color().toUpperCase()), saved.getValue().getSolutionRating());
    }
}
