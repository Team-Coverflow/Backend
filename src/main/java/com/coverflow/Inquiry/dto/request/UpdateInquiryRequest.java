package com.coverflow.Inquiry.dto.request;

public record UpdateInquiryRequest(
        long inquiryId,
        String inquiryAnswer
) {
}
