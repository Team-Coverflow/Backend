package com.coverflow.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListDTO {
    private int totalPages;
    private List<QuestionDTO> questions;

    public static QuestionListDTO of(
            final int totalPages,
            final List<QuestionDTO> questions
    ) {
        return new QuestionListDTO(totalPages, questions);
    }
}
