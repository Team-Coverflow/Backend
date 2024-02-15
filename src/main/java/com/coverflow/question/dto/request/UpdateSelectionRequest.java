package com.coverflow.question.dto.request;

public record UpdateSelectionRequest(
        Long answerId,
        boolean selection
) {
}
