package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.domain.InquiryStatus;

public record FindInquiryResponse(
        long inquiryId,
        String inquiryTitle,
        String inquiryContent,
        String inquiryAnswer,
        InquiryStatus inquiryStatus,
        String inquirerNickname
) {

    public static FindInquiryResponse from(final Inquiry inquiry) {
        return new FindInquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getInquiryStatus(),
                inquiry.getMember().getNickname()
        );
    }
}
