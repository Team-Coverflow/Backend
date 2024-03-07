package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateAnswerRequest(
        @Positive
        long answerId,
        @NotBlank
        String content
) {
}
