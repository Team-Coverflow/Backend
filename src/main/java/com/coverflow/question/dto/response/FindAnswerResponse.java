package com.coverflow.question.dto.response;

import com.coverflow.question.dto.AnswersDTO;

import java.util.List;

public record FindAnswerResponse(
        int totalPages,
        long totalElements,
        List<AnswersDTO> answers
) {

    public static FindAnswerResponse of(
            final int totalPages,
            final long totalElements,
            final List<AnswersDTO> answers
    ) {
        return new FindAnswerResponse(totalPages, totalElements, answers);
    }
}
