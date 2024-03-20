package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateSelectionRequest(
        @NotBlank
        boolean selection
) {
}
