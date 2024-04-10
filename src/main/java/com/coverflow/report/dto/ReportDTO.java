package com.coverflow.report.dto;

import com.coverflow.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private long reportId;
    private String reportContent;
    private boolean reportStatus;
    private String reporterNickname;
    private long questionId;
    private long answerId;
    private LocalDate createdAt;

    public static ReportDTO from(final Report report) {
        return new ReportDTO(
                report.getId(),
                report.getContent(),
                report.isReportStatus(),
                report.getMember().getNickname(),
                report.getQuestion().getId(),
                report.getAnswer().getId(),
                report.getCreatedAt().toLocalDate()
        );
    }
}
