package com.coverflow.question.dto.request;

public record UpdateQuestionRequest(
        long questionId,
        String title,
        String content
) {
}
