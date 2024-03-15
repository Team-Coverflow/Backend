package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.dto.InquiryDTO;

import java.util.List;

public record FindInquiryResponse(
        int totalPages,
        List<InquiryDTO> inquiryList
) {

    public static FindInquiryResponse of(
            final int totalPages,
            final List<InquiryDTO> inquiryList
    ) {
        return new FindInquiryResponse(totalPages, inquiryList);
    }
}
