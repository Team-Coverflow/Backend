package com.coverflow.board.dto.response;

import com.coverflow.board.domain.Question;

public record QuestionResponse(
        Long id,
        String title,
        String content,
        int count
) {

    public static QuestionResponse of(final Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getCount()
        );
    }
}
