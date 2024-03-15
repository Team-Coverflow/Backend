package com.coverflow.question.dto.request;

import com.coverflow.question.domain.QuestionCategory;
import com.coverflow.question.domain.QuestionTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveQuestionRequest(
        @NotBlank
        QuestionTag questionTag,
        @NotBlank
        QuestionCategory questionCategory,
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
