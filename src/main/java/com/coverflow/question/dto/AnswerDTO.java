package com.coverflow.question.dto;

import com.coverflow.question.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    private long questionId;
    private long answerId;
    private String answerContent;
    private boolean selection;
    private String answererNickname;
    private String answererTag;
    private boolean answerStatus;
    private LocalDate createAt;

    public static AnswerDTO from(final Answer answer) {
        return new AnswerDTO(
                answer.getQuestion().getId(),
                answer.getId(),
                answer.getContent(),
                answer.isSelection(),
                answer.getMember().getNickname(),
                answer.getMember().getTag(),
                answer.isAnswerStatus(),
                answer.getCreatedAt().toLocalDate()
        );
    }
}
