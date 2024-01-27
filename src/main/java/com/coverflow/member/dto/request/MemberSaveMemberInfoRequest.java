package com.coverflow.member.dto.request;

public record MemberSaveMemberInfoRequest(
        String tag,
        String age,
        String gender
) {
}
