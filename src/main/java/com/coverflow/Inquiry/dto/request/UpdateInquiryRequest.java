package com.coverflow.Inquiry.dto.request;

public record UpdateInquiryRequest(
        Long inquiryId,
        String inquiryAnswer
) {
}
