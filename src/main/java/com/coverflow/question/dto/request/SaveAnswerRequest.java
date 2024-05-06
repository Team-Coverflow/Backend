package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

public record SaveAnswerRequest(
        @NotBlank
        @Range(min = 1, max = 500)
        String content,
        @Positive
        long questionId
) {
}
