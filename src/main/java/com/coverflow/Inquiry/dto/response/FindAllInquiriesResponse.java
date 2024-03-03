package com.coverflow.Inquiry.dto.response;

import com.coverflow.Inquiry.domain.Inquiry;

public record FindAllInquiriesResponse(
        Long inquiryId,
        String inquiryTitle,
        String inquiryContent,
        String inquiryAnswer,
        String inquiryStatus,
        String inquirerNickname
) {

    public static FindAllInquiriesResponse from(final Inquiry inquiry) {
        return new FindAllInquiriesResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getStatus(),
                inquiry.getMember().getNickname()
        );
    }
}
