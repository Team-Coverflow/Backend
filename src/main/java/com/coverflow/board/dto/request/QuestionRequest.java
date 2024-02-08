package com.coverflow.board.dto.request;

public record QuestionRequest(
        Long id,
        String title,
        String content,
        Long companyId
) {
}
