package com.coverflow.report.dto.response;

import com.coverflow.report.domain.Report;
import com.coverflow.report.domain.ReportStatus;

public record FindReportResponse(
        long reportId,
        String reportContent,
        ReportStatus reportStatus,
        String reporterNickname,
        long questionId,
        long answerId
) {

    public static FindReportResponse from(final Report report) {
        return new FindReportResponse(
                report.getId(),
                report.getContent(),
                report.getReportStatus(),
                report.getMember().getNickname(),
                report.getQuestion().getId(),
                report.getAnswer().getId()
        );
    }
}
