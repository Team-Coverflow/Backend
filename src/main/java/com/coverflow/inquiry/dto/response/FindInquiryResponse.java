package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.dto.InquiryDTO;

import java.util.List;

public record FindInquiryResponse(
        int totalPages,
        List<InquiryDTO> inquiries
) {

    public static FindInquiryResponse of(
            final int totalPages,
            final List<InquiryDTO> inquiries
    ) {
        return new FindInquiryResponse(totalPages, inquiries);
    }
}
