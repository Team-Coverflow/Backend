package com.coverflow.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateInquiryRequest(

        @NotBlank
        String inquiryAnswer,
        @NotBlank
        String inquiryStatus
) {
}
