package com.coverflow.question.dto.response;

import com.coverflow.question.domain.Question;
import com.coverflow.question.domain.QuestionTag;
import com.coverflow.question.dto.AnswerDTO;

import java.time.LocalDate;
import java.util.List;

public record FindQuestionResponse(
        String companyName,
        long questionId,
        String questionTitle,
        String questionContent,
        QuestionTag questionTag,
        String questionCategory,
        int viewCount,
        int answerCount,
        int reward,
        String questionerNickname,
        String questionerTag,
        LocalDate createAt,
        int totalPages,
        List<AnswerDTO> answers
) {

    public static FindQuestionResponse of(
            final Question question,
            final int totalPages,
            final List<AnswerDTO> answers) {
        return new FindQuestionResponse(
                question.getCompany().getName(),
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getQuestionTag(),
                question.getQuestionCategory(),
                question.getViewCount(),
                question.getAnswerCount(),
                question.getReward(),
                question.getMember().getNickname(),
                question.getMember().getTag(),
                question.getCreatedAt().toLocalDate(),
                totalPages,
                answers
        );
    }
}