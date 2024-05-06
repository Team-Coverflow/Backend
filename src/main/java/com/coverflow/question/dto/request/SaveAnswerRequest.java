package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record SaveAnswerRequest(
        @NotBlank
        @Length(min = 1, max = 500)
        String content,
        @Positive
        long questionId
) {
}
