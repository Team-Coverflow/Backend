package com.coverflow.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SaveInquiryRequest(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
