package com.coverflow.question.dto.request;

import com.coverflow.question.domain.QuestionTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveQuestionRequest(
        @NotBlank
        QuestionTag questionTag,
        @NotBlank
        String questionCategory,
        @NotBlank
        String title,
        @NotBlank
        String content,
        @Positive
        long companyId,
        @Positive
        int reward
) {
}
