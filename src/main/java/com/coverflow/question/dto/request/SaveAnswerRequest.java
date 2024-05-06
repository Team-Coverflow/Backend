package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SaveAnswerRequest(
        @NotBlank
        @Size(min = 1, max = 500)
        String content,
        @Positive
        long questionId
) {
}
