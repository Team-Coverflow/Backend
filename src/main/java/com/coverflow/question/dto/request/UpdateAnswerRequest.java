package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateAnswerRequest(

        @NotBlank
        String content,
        @NotBlank
        String answerStatus
) {
}
