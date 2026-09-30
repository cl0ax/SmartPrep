package com.BTA.SmartPrep.controller;

import com.BTA.SmartPrep.domain.dto.problem.SolutionRequestDto;
import com.BTA.SmartPrep.domain.dto.problem.SolutionSubmissionDto;
import com.BTA.SmartPrep.domain.dto.submission.SubmissionHistoryDto;
import com.BTA.SmartPrep.service.SolutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/v1/solution")
public class SolutionController {
    SolutionService solutionService;

    SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }


    @PostMapping
    public ResponseEntity<SolutionSubmissionDto> getSolution(@RequestBody SolutionRequestDto requestDto) {
        try {
            SolutionSubmissionDto grade = solutionService.solutionGrade(
                    requestDto.codeString(),
                    requestDto.problemId(),
                    requestDto.userId(),
                    requestDto.categoryId()
            );
            return ResponseEntity.ok(grade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new SolutionSubmissionDto("red", 0, 0, 0.0f, new ArrayList<>(), "Error",0,"")
            );
        }
    }

    @PostMapping("/run")
    public ResponseEntity<String> getRunSolution(@RequestBody SolutionRequestDto requestDto) {
        try {
            String grade = solutionService.solutionRunGrade(
                    requestDto.codeString(),
                    requestDto.problemId(),
                    requestDto.userId(),
                    requestDto.categoryId()
            );
            return ResponseEntity.ok(grade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/submissions")
    public ResponseEntity<List<SubmissionHistoryDto>> getSubmissions(@RequestParam String userId) {
        return ResponseEntity.ok(solutionService.getSubmissions(userId));
    }
}
