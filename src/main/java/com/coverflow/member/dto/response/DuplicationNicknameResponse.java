package com.coverflow.member.dto.response;

public record DuplicationNicknameResponse(
        boolean result
) {
    public static DuplicationNicknameResponse of(final boolean result) {
        return new DuplicationNicknameResponse(
                result
        );
    }
}
