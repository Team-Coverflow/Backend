package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record SaveQuestionRequest(
        @NotBlank
        String questionTag,
        @NotBlank
        String questionCategory,
        @NotBlank
        String title,
        @NotBlank
        String content,
        @Positive
        long companyId,
        @PositiveOrZero
        int reward
) {
}
