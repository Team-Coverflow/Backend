package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveAnswerRequest(
        @NotBlank
        String content,
        @Positive
        long questionId
) {
}
