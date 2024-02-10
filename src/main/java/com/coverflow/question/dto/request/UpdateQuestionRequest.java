package com.coverflow.question.dto.request;

public record UpdateQuestionRequest(
        Long id,
        String title,
        String content
) {
}
