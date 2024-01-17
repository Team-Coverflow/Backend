package com.coverflow.member.dto.response;

import org.springframework.http.HttpStatus;

public record DuplicationNicknameResponse(
        HttpStatus status,
        String message,
        String data
) {
}
