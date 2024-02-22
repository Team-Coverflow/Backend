package com.coverflow.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    private Long answerId;
    private String answerNickname;
    private String answerTag;
    private String content;
    private LocalDateTime createAt;
}
