package com.coverflow.question.dto.request;

public record SaveQuestionRequest(
        Long id,
        String title,
        String content,
        Long companyId
) {
}
