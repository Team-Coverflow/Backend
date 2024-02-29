package com.coverflow.report.application;

import com.coverflow.member.domain.Member;
import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.Question;
import com.coverflow.question.exception.AnswerException;
import com.coverflow.question.infrastructure.AnswerRepository;
import com.coverflow.report.domain.Report;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.response.FindReportResponse;
import com.coverflow.report.exception.ReportException;
import com.coverflow.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.coverflow.report.domain.ReportType.ANSWER;
import static com.coverflow.report.domain.ReportType.QUESTION;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final AnswerRepository answerRepository;
    private final ReportRepository reportRepository;

    /**
     * [특정 회원의 신고 리스트 조회 메서드]
     */
    @Transactional(readOnly = true)
    public List<FindReportResponse> findReportsByMemberId(
            final String memberId,
            final int pageNo,
            final String criterion
    ) {
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Report> reports = reportRepository.findReportsByMemberId(memberId, pageable)
                .orElseThrow(() -> new ReportException.ReportNotFoundException(memberId));
        final List<FindReportResponse> findReports = new ArrayList<>();

        for (int i = 0; i < reports.getContent().size(); i++) {
            findReports.add(i, FindReportResponse.from(reports.getContent().get(i)));
        }
        return findReports;
    }

    /**
     * [관리자 전용: 전체 신고 리스트 조회 메서드]
     */
    @Transactional(readOnly = true)
    public List<FindReportResponse> findReports(
            final int pageNo,
            final String criterion
    ) {
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Report> reports = reportRepository.findAllReports(pageable)
                .orElseThrow(ReportException.ReportNotFoundException::new);
        final List<FindReportResponse> findReports = new ArrayList<>();

        for (int i = 0; i < reports.getContent().size(); i++) {
            findReports.add(i, FindReportResponse.from(reports.getContent().get(i)));
        }
        return findReports;
    }

    /**
     * [관리자 전용: 특정 상태 신고 조회 메서드]
     * 특정 상태(등록/삭제)의 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindReportResponse> findReportsByStatus(
            final int pageNo,
            final String criterion,
            final String status
    ) {
        final Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(criterion).descending());
        final Page<Report> reports = reportRepository.findAllByStatus(pageable, status)
                .orElseThrow(() -> new ReportException.ReportNotFoundException(status));
        final List<FindReportResponse> findReports = new ArrayList<>();

        for (int i = 0; i < reports.getContent().size(); i++) {
            findReports.add(i, FindReportResponse.from(reports.getContent().get(i)));
        }
        return findReports;
    }

    /**
     * [신고 등록 메서드]
     */
    @Transactional
    public void saveReport(
            final SaveReportRequest request,
            final String memberId
    ) {
        Report report = Report.builder().build();

        if ((QUESTION).equals(request.type())) {
            report = Report.builder()
                    .content(request.content())
                    .status("등록")
                    .member(Member.builder()
                            .id(UUID.fromString(memberId))
                            .build())
                    .question(Question.builder()
                            .id(request.id())
                            .build())
                    .build();
        }
        if ((ANSWER).equals(request.type())) {
            Answer answer = answerRepository.findByIdAndStatus(request.id(), "등록")
                    .orElseThrow(() -> new AnswerException.AnswerNotFoundException(request.id()));

            report = Report.builder()
                    .content(request.content())
                    .status("등록")
                    .member(Member.builder()
                            .id(UUID.fromString(memberId))
                            .build())
                    .question(Question.builder()
                            .id(answer.getId())
                            .build())
                    .answer(Answer.builder()
                            .id(request.id())
                            .build())
                    .build();
        }

        reportRepository.save(report);
    }

    /**
     * [관리자 전용: 신고 삭제 메서드]
     */
    @Transactional
    public void deleteReport(final Long reportId) {
        final Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException.ReportNotFoundException(reportId));

        report.updateStatus("삭제");
    }
}
