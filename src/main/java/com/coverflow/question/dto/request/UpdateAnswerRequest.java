package com.coverflow.question.dto.request;

public record UpdateAnswerRequest(
        Long id,
        String content
) {
}
