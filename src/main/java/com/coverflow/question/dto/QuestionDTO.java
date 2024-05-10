package com.coverflow.question.dto;

import com.coverflow.question.domain.Question;
import com.coverflow.question.domain.QuestionTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private long questionId;
    private String companyName;
    private String questionerNickname;
    private String questionerTag;
    private String questionTitle;
    private String questionContent;
    private QuestionTag questionTag;
    private String questionCategory;
    private long questionViewCount;
    private int answerCount;
    private int reward;
    private boolean questionStatus;
    private LocalDate createAt;

    public static QuestionDTO from(final Question question) {
        return new QuestionDTO(
                question.getId(),
                question.getCompany().getName(),
                question.getMember().getNickname(),
                question.getMember().getTag(),
                question.getTitle(),
                question.getContent(),
                question.getQuestionTag(),
                question.getQuestionCategory(),
                question.getViewCount(),
                question.getAnswers().size(),
                question.getReward(),
                question.isQuestionStatus(),
                question.getCreatedAt().toLocalDate()
        );
    }
}
