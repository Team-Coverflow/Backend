package com.coverflow.report.dto.request;

import com.coverflow.report.domain.ReportType;

public record SaveReportRequest(
        String content,
        ReportType type,
        long id

) {
}
