package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateAnswerRequest(

        @NotBlank
        @Length(min = 1, max = 500)
        String content,
        @NotBlank
        boolean answerStatus
) {
}
