package com.coverflow.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAndQuestionDTO {
    private int totalPages;
    private long totalElements;
    private List<QuestionDTO> questions;

    public static CompanyAndQuestionDTO of(
            final int totalPages,
            final long totalElements,
            final List<QuestionDTO> questions
    ) {
        return new CompanyAndQuestionDTO(totalPages, totalElements, questions);
    }
}
