package com.coverflow.inquiry.dto.request;

public record FindInquiryAdminRequest(
        String createdStartDate,
        String createdEndDate,
        String status
) {
}
