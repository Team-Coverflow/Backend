package com.coverflow.question.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateSelectionRequest(
        @NotNull
        boolean selection
) {
}
