package com.coverflow.report.dto.response;

import com.coverflow.report.domain.Report;

public record FindReportResponse(
        Long reportId,
        String reportContent,
        String reportStatus,
        String reporterNickname,
        Long questionId,
        Long answerId
) {

    public static FindReportResponse from(final Report report) {
        return new FindReportResponse(
                report.getId(),
                report.getContent(),
                report.getStatus(),
                report.getMember().getNickname(),
                report.getQuestion().getId(),
                report.getAnswer().getId()
        );
    }
}
