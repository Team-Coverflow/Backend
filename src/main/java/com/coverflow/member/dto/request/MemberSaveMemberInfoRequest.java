package com.coverflow.member.dto.request;

public record MemberSaveMemberInfoRequest(
        String nickname,
        String tag,
        String age,
        String gender
) {
}
