package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateSelectionRequest(
        @Positive
        long answerId,
        @NotBlank
        boolean selection
) {
}
