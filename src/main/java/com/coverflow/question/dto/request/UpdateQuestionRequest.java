package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateQuestionRequest(
        @Positive
        long questionId,
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
