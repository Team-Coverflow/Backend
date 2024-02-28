package com.coverflow.member.dto.response;

import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.Role;
import com.coverflow.member.domain.SocialType;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindAllMembersResponse(
        UUID id,
        String email,
        String nickname,
        String tag,
        String age,
        String gender,
        int fishShapedBun,
        String status,
        LocalDateTime connectedAt,
        Role role,
        SocialType socialType
) {

    public static FindAllMembersResponse from(final Member member) {
        return new FindAllMembersResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getTag(),
                member.getAge(),
                member.getGender(),
                member.getFishShapedBun(),
                member.getStatus(),
                member.getConnectedAt(),
                member.getRole(),
                member.getSocialType()
        );
    }
}
