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
    private String reportedNickname;
    private String reporterNickname;
    private Long questionId;
    private Long answerId;
    private LocalDate createdAt;

    public static ReportDTO from(final Report report) {
        Long answerId = null;
        String nickname = report.getQuestion().getMember().getNickname();

        if (report.getAnswer() != null) {
            answerId = report.getAnswer().getId();
            nickname = report.getAnswer().getMember().getNickname();
        }
        return new ReportDTO(
                report.getId(),
                report.getContent(),
                report.getReportStatus(),
                report.getType(),
                nickname,
                report.getMember().getNickname(),
                report.getQuestion().getId(),
                answerId,
                report.getCreatedAt().toLocalDate()
        );
    }
}
