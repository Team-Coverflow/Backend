package com.coverflow.question.dto.request;

public record UpdateQuestionRequest(
        Long questionId,
        String title,
        String content
) {
}
