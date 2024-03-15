package com.coverflow.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerListDTO {
    private int totalPages;
    private List<AnswerDTO> answers;

    public static AnswerListDTO of(
            final int totalPages,
            final List<AnswerDTO> answers
    ) {
        return new AnswerListDTO(totalPages, answers);
    }
}
