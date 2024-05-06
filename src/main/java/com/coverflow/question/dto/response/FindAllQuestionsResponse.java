package com.coverflow.question.dto.response;

import com.coverflow.question.dto.QuestionDTO;

import java.util.List;

public record FindAllQuestionsResponse(
        int totalPages,
        long totalElements,
        List<QuestionDTO> questions
) {

    public static FindAllQuestionsResponse of(
            final int totalPages,
            final long totalElements,
            final List<QuestionDTO> questions
    ) {
        return new FindAllQuestionsResponse(totalPages, totalElements, questions);
    }
}
