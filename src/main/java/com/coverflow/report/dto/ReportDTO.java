package com.coverflow.report.dto;

import com.coverflow.report.domain.Report;
import com.coverflow.report.domain.ReportType;
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
    private Boolean reportStatus;
    private ReportType reportType;
    private String reporterNickname;
    private Long questionId;
    private Long answerId;
    private LocalDate createdAt;

    public static ReportDTO from(final Report report) {
        Long answerId = null;
        if (report.getAnswer() != null) {
            answerId = report.getAnswer().getId();
        }
        return new ReportDTO(
                report.getId(),
                report.getContent(),
                report.getReportStatus(),
                report.getType(),
                report.getMember().getNickname(),
                report.getQuestion().getId(),
                answerId,
                report.getCreatedAt().toLocalDate()
        );
    }
}
