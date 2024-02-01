package com.coverflow.member.dto.response;

public record UpdateNicknameResponse(
        String nickname
) {
    public static UpdateNicknameResponse of(String nickname) {
        return new UpdateNicknameResponse(nickname);
    }
}
