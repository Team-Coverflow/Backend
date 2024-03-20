package com.coverflow.member.dto.response;

import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberStatus;
import com.coverflow.member.domain.Role;
import com.coverflow.member.domain.SocialType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FindMemberResponse(
        String email,
        String nickname,
        String tag,
        String age,
        String gender,
        int fishShapedBun,
        MemberStatus memberStatus,
        LocalDate createdAt,
        LocalDateTime connectedAt,
        Role role,
        SocialType socialType
) {
    public static FindMemberResponse from(final Member member) {
        return new FindMemberResponse(
                member.getEmail(),
                member.getNickname(),
                member.getTag(),
                member.getAge(),
                member.getGender(),
                member.getFishShapedBun(),
                member.getMemberStatus(),
                member.getCreatedAt().toLocalDate(),
                member.getConnectedAt(),
                member.getRole(),
                member.getSocialType()
        );
    }
}
