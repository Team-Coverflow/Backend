package com.coverflow.board.dto.request;

public record SaveAnswerRequest(
        Long questionId,
        String content
) {
}
