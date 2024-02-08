package com.coverflow.board.dto.request;

public record QuestionRequest(
        String title,
        String content,
        Long companyId
) {
}
