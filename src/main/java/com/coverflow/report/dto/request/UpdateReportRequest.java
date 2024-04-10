package com.coverflow.report.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateReportRequest(

        @NotBlank
        boolean updateStatus
) {
}
