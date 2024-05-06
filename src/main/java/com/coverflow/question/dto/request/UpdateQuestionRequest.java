package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateQuestionRequest(

        @NotBlank
        @Length(min = 1, max = 100)
        String title,
        @NotBlank
        @Length(min = 1, max = 500)
        String content,
        boolean questionStatus
) {
}
