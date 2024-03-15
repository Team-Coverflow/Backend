package com.coverflow.report.dto;

import com.coverflow.report.domain.Report;
import com.coverflow.report.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private long reportId;
    private String reportContent;
    private ReportStatus reportStatus;
    private String reporterNickname;
    private long questionId;
    private long answerId;

    public static ReportDTO from(final Report report) {
        return new ReportDTO(
                report.getId(),
                report.getContent(),
                report.getReportStatus(),
                report.getMember().getNickname(),
                report.getQuestion().getId(),
                report.getAnswer().getId()
        );
    }
}
