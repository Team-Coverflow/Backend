package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.dto.InquiriesDTO;

import java.util.List;

public record FindAllInquiriesResponse(
        int totalPages,
        List<InquiriesDTO> inquiries
) {

    public static FindAllInquiriesResponse of(
            final int totalPages,
            final List<InquiriesDTO> inquiries
    ) {
        return new FindAllInquiriesResponse(totalPages, inquiries);
    }
}
