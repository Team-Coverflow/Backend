package com.coverflow.Inquiry.dto.response;

import com.coverflow.Inquiry.domain.Inquiry;

public record FindAllInquiriesResponse(
        Long inquiryId,
        String inquiryContent,
        String inquiryStatus,
        String inquirerNickname
) {

    public static FindAllInquiriesResponse from(final Inquiry inquiry) {
        return new FindAllInquiriesResponse(
                inquiry.getId(),
                inquiry.getContent(),
                inquiry.getStatus(),
                inquiry.getMember().getNickname()
        );
    }
}
