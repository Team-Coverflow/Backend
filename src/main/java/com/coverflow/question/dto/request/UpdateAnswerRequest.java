package com.coverflow.question.dto.request;

public record UpdateAnswerRequest(
        Long answerId,
        String content
) {
}
