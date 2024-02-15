package com.coverflow.question.dto.response;

import com.coverflow.question.domain.Question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record FindQuestionResponse(
        Long questionId,
        String title,
        String questionContent,
        int viewCount,
        int answerCount,
        int reward,
        String questionNickname,
        String questionTag,
        LocalDateTime createAt,
        List<FindAnswerResponse> answers
) {

    public static FindQuestionResponse from(final Question question) {
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
                question.getAnswers().stream()
                        .map(answer -> new FindAnswerResponse(
                                answer.getId(),
                                answer.getContent(),
                                answer.getMember().getNickname(),
                                answer.getMember().getTag(),
                                answer.getCreatedAt()
                        ))
                        .collect(Collectors.toList())
        );
    }
}