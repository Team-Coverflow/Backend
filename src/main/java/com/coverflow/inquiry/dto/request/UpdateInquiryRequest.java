package com.coverflow.inquiry.dto.request;

public record UpdateInquiryRequest(
        long inquiryId,
        String inquiryAnswer
) {
}
