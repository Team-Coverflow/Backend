package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveQuestionRequest(
        @NotBlank
        String title,
        @NotBlank
        String content,
        @Positive
        long companyId,
        @Positive
        int reward
) {
}
