package com.coverflow.enquiry.dto.response;

import com.coverflow.enquiry.domain.Enquiry;

import java.util.UUID;

public record FindAllEnquiriesResponse(
        Long enquiryId,
        String content,
        String status,
        UUID memberId
) {

    public static FindAllEnquiriesResponse from(final Enquiry enquiry) {
        return new FindAllEnquiriesResponse(
                enquiry.getId(),
                enquiry.getContent(),
                enquiry.getStatus(),
                enquiry.getMember().getId()
        );
    }
}
