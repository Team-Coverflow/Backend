package com.coverflow.question.dto;

import com.coverflow.question.domain.Question;
import com.coverflow.question.domain.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsDTO {

    private long questionId;
    private String companyName;
    private String questionerNickname;
    private String questionerTag;
    private String questionTitle;
    private int questionViewCount;
    private int answerCount;
    private int reward;
    private QuestionStatus questionStatus;
    private LocalDate createAt;

    public static QuestionsDTO from(final Question question) {
        return new QuestionsDTO(
                question.getId(),
                question.getCompany().getName(),
                question.getMember().getNickname(),
                question.getMember().getTag(),
                question.getTitle(),
                question.getViewCount(),
                question.getAnswerCount(),
                question.getReward(),
                question.getQuestionStatus(),
                question.getCreatedAt().toLocalDate()
        );
    }
}
