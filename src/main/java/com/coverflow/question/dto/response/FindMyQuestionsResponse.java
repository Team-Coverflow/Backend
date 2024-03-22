package com.coverflow.question.dto.response;

import com.coverflow.question.dto.MyQuestionDTO;

import java.util.List;

public record FindMyQuestionsResponse(
        int totalPages,
        long totalElements,
        List<MyQuestionDTO> questions
) {

    public static FindMyQuestionsResponse of(
            final int totalPages,
            final long totalElements,
            final List<MyQuestionDTO> questions
    ) {
        return new FindMyQuestionsResponse(totalPages, totalElements, questions);
    }
}