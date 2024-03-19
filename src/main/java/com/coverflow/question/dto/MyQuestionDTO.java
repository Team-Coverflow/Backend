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
public class MyQuestionDTO {
    private long questionId;
    private String companyName;
    private String questionerNickname;
    private String questionerTag;
    private String questionTitle;
    private QuestionTag questionTag;
    private String questionCategory;
    private int questionViewCount;
    private int answerCount;
    private int reward;
    private LocalDate createAt;

    public static MyQuestionDTO from(final Question question) {
        return new MyQuestionDTO(
                question.getId(),
                question.getCompany().getName(),
                question.getMember().getNickname(),
                question.getMember().getTag(),
                question.getTitle(),
                question.getQuestionTag(),
                question.getQuestionCategory(),
                question.getViewCount(),
                question.getAnswerCount(),
                question.getReward(),
                question.getCreatedAt().toLocalDate()
        );
    }
}
