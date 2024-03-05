package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.domain.Inquiry;

public record FindInquiryResponse(
        long inquiryId,
        String inquiryTitle,
        String inquiryContent,
        String inquiryAnswer,
        String inquiryStatus,
        String inquirerNickname
) {

    public static FindInquiryResponse from(final Inquiry inquiry) {
        return new FindInquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getStatus(),
                inquiry.getMember().getNickname()
        );
    }
}
