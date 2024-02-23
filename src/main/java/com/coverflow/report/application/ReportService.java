package com.coverflow.report.application;

import com.coverflow.report.domain.Report;
import com.coverflow.report.dto.response.FindReportResponse;
import com.coverflow.report.exception.ReportException;
import com.coverflow.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    /**
     * [특정 회원의 신고 리스트 조회 메서드]
     */
    public List<FindReportResponse> findReportsByMemberId(final UUID memberId) {
        final List<Report> reports = reportRepository.findReportsByMemberId(memberId)
                .orElseThrow(() -> new ReportException.ReportNotFoundException(memberId));
        final List<FindReportResponse> findReports = new ArrayList<>();

        for (int i = 0; i < reports.size(); i++) {
            findReports.add(i, FindReportResponse.from(reports.get(i)));
        }
        return findReports;
    }

    /**
     * [전체 신고 리스트 조회 메서드]
     */
    public List<FindReportResponse> findReports() {
        final List<Report> reports = reportRepository.findReports()
                .orElseThrow(ReportException.ReportNotFoundException::new);
        final List<FindReportResponse> findReports = new ArrayList<>();

        for (int i = 0; i < reports.size(); i++) {
            findReports.add(i, FindReportResponse.from(reports.get(i)));
        }
        return findReports;
    }
}
