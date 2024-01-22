package com.coverflow.member.dto.response;

public record MemberVerifyDuplicationNicknameResponse(
        boolean result
) {
    public static MemberVerifyDuplicationNicknameResponse of(final boolean result) {
        return new MemberVerifyDuplicationNicknameResponse(
                result
        );
    }
}
