package com.BTA.SmartPrep.domain.dto.submission;

import com.BTA.SmartPrep.domain.entity.SolutionRating;

import java.time.LocalDateTime;

public record SubmissionHistoryDto(
        String submissionId,
        String problemTitle,
        SolutionRating rating,
        LocalDateTime submittedAt,
        String answer
) {
}
