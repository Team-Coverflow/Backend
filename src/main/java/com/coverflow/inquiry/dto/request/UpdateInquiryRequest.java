package com.coverflow.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateInquiryRequest(
        @Positive
        long inquiryId,
        @NotBlank
        String inquiryAnswer
) {
}
