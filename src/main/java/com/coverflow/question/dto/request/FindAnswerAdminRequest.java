package com.coverflow.question.dto.request;

public record FindAnswerAdminRequest(
        String createdStartDate,
        String createdEndDate,
        Boolean status
) {
}
