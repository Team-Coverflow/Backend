package com.coverflow.member.dto.request;

public record UpdateMemberRequest(
        String tag,
        String age,
        String gender
) {
}
