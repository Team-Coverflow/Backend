package com.coverflow.question.dto.response;

import com.coverflow.question.domain.Answer;

import java.time.LocalDateTime;

public record FindAnswerResponse(
        Long answerId,
        String answerContent,
        String answerNickname,
        String answerTag,
        LocalDateTime createAt
) {

    public static FindAnswerResponse from(final Answer answer) {
        return new FindAnswerResponse(
                answer.getId(),
                answer.getContent(),
                answer.getMember().getNickname(),
                answer.getMember().getTag(),
                answer.getCreatedAt()
        );
    }
}
