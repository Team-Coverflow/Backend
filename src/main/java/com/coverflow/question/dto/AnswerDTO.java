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

    private long answerId;
    private String answererNickname;
    private String answererTag;
    private String answerContent;
    private LocalDate createAt;

    public static AnswerDTO from(final Answer answer) {
        return new AnswerDTO(
                answer.getId(),
                answer.getMember().getNickname(),
                answer.getMember().getTag(),
                answer.getContent(),
                answer.getCreatedAt().toLocalDate()
        );
    }
}
