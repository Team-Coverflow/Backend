package com.coverflow.question.dto.request;

public record SaveAnswerRequest(
        String content,
        long questionId
) {
}
