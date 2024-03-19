package com.coverflow.question.dto;

import com.coverflow.question.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyAnswerDTO {
    private long answerId;
    private String companyName;
    private String questionTitle;
    private String answerContent;
    private boolean selection;
    private String answererNickname;
    private String answererTag;
    private LocalDate createAt;

    public static MyAnswerDTO from(final Answer answer) {
        return new MyAnswerDTO(
                answer.getId(),
                answer.getQuestion().getCompany().getName(),
                answer.getQuestion().getTitle(),
                answer.getContent(),
                answer.isSelection(),
                answer.getMember().getNickname(),
                answer.getMember().getTag(),
                answer.getCreatedAt().toLocalDate()
        );
    }
}
