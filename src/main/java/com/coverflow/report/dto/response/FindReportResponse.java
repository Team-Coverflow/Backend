package com.coverflow.report.dto.response;

import com.coverflow.report.domain.Report;

import java.util.UUID;

public record FindReportResponse(
        Long reportId,
        String content,
        String status,
        UUID memberId,
        Long questionId,
        Long answerId
) {

    public static FindReportResponse from(final Report report) {
        return new FindReportResponse(
                report.getId(),
                report.getContent(),
                report.getStatus(),
                report.getMember().getId(),
                report.getQuestion().getId(),
                report.getAnswer().getId()
        );
    }
}
