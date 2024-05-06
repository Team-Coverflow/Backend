package com.coverflow.question.dto.response;

import com.coverflow.question.dto.AnswerDTO;

import java.util.List;

public record FindAnswerResponse(
        int totalPages,
        long totalElements,
        List<AnswerDTO> answers
) {

    public static FindAnswerResponse of(
            final int totalPages,
            final long totalElements,
            final List<AnswerDTO> answers
    ) {
        return new FindAnswerResponse(totalPages, totalElements, answers);
    }
}
