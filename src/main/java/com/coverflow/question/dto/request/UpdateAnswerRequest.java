package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public record UpdateAnswerRequest(

        @NotBlank
        @Range(min = 1, max = 500)
        String content,
        @NotBlank
        boolean answerStatus
) {
}
