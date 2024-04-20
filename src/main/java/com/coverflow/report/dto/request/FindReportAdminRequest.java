package com.coverflow.report.dto.request;

public record FindReportAdminRequest(
        String createdStartDate,
        String createdEndDate,
        String content,
        String status,
        String type
) {
}
