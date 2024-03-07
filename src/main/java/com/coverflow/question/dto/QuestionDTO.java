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
public class QuestionDTO {

    private long questionId;
    private String questionerNickname;
    private String questionerTag;
    private String questionTitle;
    private String questionContent;
    private int questionViewCount;
    private int answerCount;
    private int reward;
    private LocalDateTime createAt;
}
