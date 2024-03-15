package com.coverflow.inquiry.dto.response;

import com.coverflow.inquiry.dto.InquiriesDTO;

import java.util.List;

public record FindAllInquiriesResponse(
        int totalPages,
        List<InquiriesDTO> inquiriesList
) {

    public static FindAllInquiriesResponse of(
            final int totalPages,
            final List<InquiriesDTO> inquiriesList
    ) {
        return new FindAllInquiriesResponse(totalPages, inquiriesList);
    }
}
