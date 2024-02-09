package com.coverflow.board.dto.request;

public record SaveAnswerRequest(
        String content,
        Long questionId
) {
}
