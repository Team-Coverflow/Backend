package com.coverflow.question.dto.response;

import com.coverflow.question.domain.Question;

import java.time.LocalDateTime;

public record FindAllQuestionsResponse(
        Long questionId,
        String companyName,
        String questionNickname,
        String questionTitle,
        int viewCount,
        int answerCount,
        int reward,
        LocalDateTime createAt
) {

    public static FindAllQuestionsResponse from(final Question question) {
        return new FindAllQuestionsResponse(
                question.getId(),
                question.getCompany().getName(),
                question.getMember().getNickname(),
                question.getTitle(),
                question.getViewCount(),
                question.getAnswerCount(),
                question.getReward(),
                question.getCreatedAt()
        );
    }
}
