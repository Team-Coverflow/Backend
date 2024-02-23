package com.coverflow.enquiry.dto.response;

import java.util.UUID;

public record FindEnquiryResponse(
        Long enquiryId,
        String content,
        String status,
        UUID memberId
) {
}
