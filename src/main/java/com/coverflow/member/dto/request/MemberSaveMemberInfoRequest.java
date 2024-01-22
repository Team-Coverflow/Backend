package com.coverflow.member.dto.request;

public record MemberSaveMemberInfoRequest(
        String nickname,
        String age,
        String gender
) {
}
