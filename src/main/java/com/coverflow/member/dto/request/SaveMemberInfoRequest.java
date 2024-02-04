package com.coverflow.member.dto.request;

public record SaveMemberInfoRequest(
        String tag,
        String age,
        String gender
) {
}
