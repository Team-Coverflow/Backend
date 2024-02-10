package com.coverflow.member.dto.response;

public record UpdateNicknameResponse(
        String nickname
) {
    public static UpdateNicknameResponse from(final String nickname) {
        return new UpdateNicknameResponse(nickname);
    }
}
