package com.coverflow.member.dto.response;

import com.coverflow.member.dto.MembersDTO;

import java.util.List;

public record FindAllMembersResponse(
        int totalPages,
        List<MembersDTO> members
) {

    public static FindAllMembersResponse of(
            final int totalPages,
            final List<MembersDTO> members
    ) {
        return new FindAllMembersResponse(totalPages, members);
    }
}
