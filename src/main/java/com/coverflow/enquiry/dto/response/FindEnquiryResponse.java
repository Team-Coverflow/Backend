package com.coverflow.enquiry.dto.response;

import com.coverflow.enquiry.domain.Enquiry;

import java.util.UUID;

public record FindEnquiryResponse(
        Long enquiryId,
        String content,
        String status,
        UUID memberId
) {

    public static FindEnquiryResponse from(final Enquiry enquiry) {
        return new FindEnquiryResponse(
                enquiry.getId(),
                enquiry.getContent(),
                enquiry.getStatus(),
                enquiry.getMember().getId()
        );
    }
}
