package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.domain.InquiryStatus;
import com.coverflow.inquiry.dto.InquiryCountDTO;

import java.time.LocalDateTime;

public record FindInquiryResponse(
        long inquiryId,
        String inquiryTitle,
        String inquiryContent,
        String inquiryAnswer,
        InquiryStatus inquiryStatus,
        String inquirerNickname,
        LocalDateTime createdAt,
        int allInquiriesCount,
        int waitInquiriesCount,
        int completeInquiriesCount
) {

    public static FindInquiryResponse of(
            final Inquiry inquiry,
            final InquiryCountDTO inquiryCountDTO
    ) {
        return new FindInquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getInquiryStatus(),
                inquiry.getMember().getNickname(),
                inquiry.getCreatedAt(),
                inquiryCountDTO.getAllInquiryCount(),
                inquiryCountDTO.getWaitInquiryCount(),
                inquiryCountDTO.getCompleteInquiryCount()
        );
    }
}
