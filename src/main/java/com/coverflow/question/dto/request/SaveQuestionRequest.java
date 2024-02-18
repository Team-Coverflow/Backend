package com.coverflow.question.dto.request;

public record SaveQuestionRequest(
        String title,
        String content,
        Long companyId,
        int reward
) {
}
