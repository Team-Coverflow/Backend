package com.coverflow.question.dto.response;

import com.coverflow.question.dto.MyAnswerDTO;

import java.util.List;

public record FindMyAnswersResponse(
        int totalPages,
        long totalElements,
        List<MyAnswerDTO> answers
) {

    public static FindMyAnswersResponse of(
            final int totalPages,
            final long totalElements,
            final List<MyAnswerDTO> answers
    ) {
        return new FindMyAnswersResponse(totalPages, totalElements, answers);
    }
}
