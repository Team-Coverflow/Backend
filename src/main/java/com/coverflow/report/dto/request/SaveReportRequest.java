package com.coverflow.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveReportRequest(
        @NotBlank
        String content,
        @NotBlank
        String type,
        @Positive
        long id
) {
}
