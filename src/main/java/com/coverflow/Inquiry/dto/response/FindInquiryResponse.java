package com.coverflow.Inquiry.dto.response;

import com.coverflow.Inquiry.domain.Inquiry;

public record FindInquiryResponse(
        Long inquiryId,
        String inquiryContent,
        String inquiryStatus,
        String inquirerNickname
) {

    public static FindInquiryResponse from(final Inquiry inquiry) {
        return new FindInquiryResponse(
                inquiry.getId(),
                inquiry.getContent(),
                inquiry.getStatus(),
                inquiry.getMember().getNickname()
        );
    }
}
