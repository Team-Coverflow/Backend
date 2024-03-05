package com.coverflow.question.dto.request;

public record UpdateSelectionRequest(
        long answerId,
        boolean selection
) {
}
