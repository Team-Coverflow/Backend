package com.coverflow.question.dto.response;

import com.coverflow.question.domain.Answer;

import java.time.LocalDateTime;

public record FindAnswerResponse(
        Long id,
        String content,
        LocalDateTime createAt,
        String nickname,
        String tag
) {

    public static FindAnswerResponse from(final Answer answer) {
        return new FindAnswerResponse(
                answer.getId(),
                answer.getContent(),
                answer.getCreatedAt(),
                answer.getMember().getNickname(),
                answer.getMember().getTag()
        );
    }
}
