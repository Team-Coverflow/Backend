package com.coverflow.report.dto.request;

import com.coverflow.report.domain.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveReportRequest(
        @NotBlank
        String content,
        @NotBlank
        ReportType type,
        @Positive
        long id

) {
}
