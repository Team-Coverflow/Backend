package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public record UpdateQuestionRequest(

        @NotBlank
        @Range(min = 1, max = 100)
        String title,
        @NotBlank
        @Range(min = 1, max = 500)
        String content,
        boolean questionStatus
) {
}
