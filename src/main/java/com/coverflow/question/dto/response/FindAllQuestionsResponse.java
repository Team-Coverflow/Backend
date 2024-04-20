package com.coverflow.question.dto.response;

import com.coverflow.question.dto.QuestionsDTO;

import java.util.List;

public record FindAllQuestionsResponse(
        int totalPages,
        long totalElements,
        List<QuestionsDTO> questions
) {

    public static FindAllQuestionsResponse of(
            final int totalPages,
            final long totalElements,
            final List<QuestionsDTO> questions
    ) {
        return new FindAllQuestionsResponse(totalPages, totalElements, questions);
    }
}
