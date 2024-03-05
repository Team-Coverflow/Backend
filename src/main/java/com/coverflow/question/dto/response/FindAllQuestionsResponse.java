package com.coverflow.question.dto.response;

import com.coverflow.question.domain.Question;

import java.time.LocalDateTime;

public record FindAllQuestionsResponse(
        long questionId,
        String companyName,
        String questionerNickname,
        String questionerTag,
        String questionTitle,
        int viewCount,
        int answerCount,
        int reward,
        String questionStatus,
        LocalDateTime createAt
) {

    public static FindAllQuestionsResponse from(final Question question) {
        return new FindAllQuestionsResponse(
                question.getId(),
                question.getCompany().getName(),
                question.getMember().getNickname(),
                question.getMember().getTag(),
                question.getTitle(),
                question.getViewCount(),
                question.getAnswerCount(),
                question.getReward(),
                question.getStatus(),
                question.getCreatedAt()
        );
    }
}
