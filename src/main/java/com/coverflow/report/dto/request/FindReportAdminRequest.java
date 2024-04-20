package com.coverflow.report.dto.request;

public record FindReportAdminRequest(
        String createdStartDate,
        String createdEndDate,
        String content,
        Boolean status,
        String type
) {
}
