package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record SaveQuestionRequest(
        @NotBlank
        String questionTag,
        @NotBlank
        String questionCategory,
        @NotBlank
        @Size(min = 1, max = 100)
        String title,
        @NotBlank
        @Size(min = 1, max = 500)
        String content,
        @Positive
        long companyId,
        @PositiveOrZero
        int reward
) {
}
