package com.coverflow.question.dto.response;

import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.AnswerDTO;

import java.time.LocalDateTime;
import java.util.List;

public record FindQuestionResponse(
        Long questionId,
        String questionTitle,
        String questionContent,
        int viewCount,
        int answerCount,
        int reward,
        String questionerNickname,
        String questionerTag,
        LocalDateTime createAt,
        List<AnswerDTO> answers
) {

    public static FindQuestionResponse of(
            final Question question,
            final List<AnswerDTO> answers) {
        return new FindQuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getViewCount(),
                question.getAnswerCount(),
                question.getReward(),
                question.getMember().getNickname(),
                question.getMember().getTag(),
                question.getCreatedAt(),
                answers
        );
    }
}