package com.coverflow.question.dto.request;

public record SaveQuestionRequest(
        String title,
        String content,
        long companyId,
        int reward
) {
}
