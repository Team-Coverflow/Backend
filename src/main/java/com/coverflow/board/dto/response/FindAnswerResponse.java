package com.coverflow.board.dto.response;

import java.time.LocalDateTime;

public record FindAnswerResponse(
        Long id,
        String content,
        LocalDateTime createAt,
        String nickname,
        String tag
) {
}
