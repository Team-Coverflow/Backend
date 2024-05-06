package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateQuestionRequest(

        @NotBlank
        String title,
        @NotBlank
        String content,
        boolean questionStatus
) {
}
