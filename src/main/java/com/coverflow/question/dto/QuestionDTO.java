package com.coverflow.question.dto;

import com.coverflow.question.domain.Question;
import com.coverflow.question.domain.QuestionCategory;
import com.coverflow.question.domain.QuestionTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private long questionId;
    private String questionerNickname;
    private String questionerTag;
    private String questionTitle;
    private String questionContent;
    private QuestionTag questionTag;
    private QuestionCategory questionCategory;
    private int questionViewCount;
    private int answerCount;
    private int reward;
    private LocalDateTime createAt;

    public static QuestionDTO from(final Question question) {
        return new QuestionDTO(
                question.getId(),
                question.getMember().getNickname(),
                question.getMember().getTag(),
                question.getTitle(),
                question.getContent(),
                question.getQuestionTag(),
                question.getQuestionCategory(),
                question.getViewCount(),
                question.getAnswerCount(),
                question.getReward(),
                question.getCreatedAt()
        );
    }
}
