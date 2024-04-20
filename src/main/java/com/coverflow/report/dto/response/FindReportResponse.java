package com.coverflow.report.dto.response;

import com.coverflow.report.dto.ReportDTO;

import java.util.List;

public record FindReportResponse(
        int totalPages,
        long totalElements,
        List<ReportDTO> reports
) {

    public static FindReportResponse of(
            final int totalPages,
            final long totalElements,
            final List<ReportDTO> reports) {
        return new FindReportResponse(totalPages, totalElements, reports);
    }
}
