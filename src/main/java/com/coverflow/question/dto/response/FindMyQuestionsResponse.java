package com.coverflow.question.dto.response;

import com.coverflow.question.dto.MyQuestionDTO;

import java.util.List;

public record FindMyQuestionsResponse(
        int totalPages,
        List<MyQuestionDTO> questions
) {

    public static FindMyQuestionsResponse of(
            final int totalPages,
            final List<MyQuestionDTO> questions
    ) {
        return new FindMyQuestionsResponse(totalPages, questions);
    }
}