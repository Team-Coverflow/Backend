package com.coverflow.member.dto.request;

public record SaveMemberRequest(
        String tag,
        String age,
        String gender
) {
}
