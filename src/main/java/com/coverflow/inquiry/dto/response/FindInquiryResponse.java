package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.dto.InquiryDTO;

import java.util.List;

public record FindInquiryResponse(
        int totalPages,
        long totalElements,
        List<InquiryDTO> inquiries
) {

    public static FindInquiryResponse of(
            final int totalPages,
            final long totalElements,
            final List<InquiryDTO> inquiries
    ) {
        return new FindInquiryResponse(totalPages, totalElements, inquiries);
    }
}
