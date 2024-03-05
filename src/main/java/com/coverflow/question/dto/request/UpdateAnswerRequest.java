package com.coverflow.question.dto.request;

public record UpdateAnswerRequest(
        long answerId,
        String content
) {
}
