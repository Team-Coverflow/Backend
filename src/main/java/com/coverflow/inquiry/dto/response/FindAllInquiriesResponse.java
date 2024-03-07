package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.domain.InquiryStatus;

import java.time.LocalDateTime;

public record FindAllInquiriesResponse(
        long inquiryId,
        String inquiryTitle,
        String inquiryContent,
        String inquiryAnswer,
        InquiryStatus inquiryStatus,
        String inquirerNickname,
        LocalDateTime createdAt
) {

    public static FindAllInquiriesResponse from(final Inquiry inquiry) {
        return new FindAllInquiriesResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getInquiryStatus(),
                inquiry.getMember().getNickname(),
                inquiry.getCreatedAt()
        );
    }
}
