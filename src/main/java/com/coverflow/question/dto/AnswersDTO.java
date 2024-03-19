package com.coverflow.question.dto;

import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.AnswerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswersDTO {
    private long answerId;
    private String answerContent;
    private boolean selection;
    private String answererNickname;
    private String answererTag;
    private AnswerStatus answerStatus;
    private LocalDate createAt;

    public static AnswersDTO from(final Answer answer) {
        return new AnswersDTO(
                answer.getId(),
                answer.getContent(),
                answer.isSelection(),
                answer.getMember().getNickname(),
                answer.getMember().getTag(),
                answer.getAnswerStatus(),
                answer.getCreatedAt().toLocalDate()
        );
    }
}