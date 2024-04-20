package com.coverflow.question.dto.request;

public record FindQuestionAdminRequest(
        String createdStartDate,
        String createdEndDate,
        String status
) {
}