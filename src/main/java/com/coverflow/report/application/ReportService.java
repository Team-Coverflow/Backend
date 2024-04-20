package com.coverflow.report.application;

import com.coverflow.question.domain.Answer;
import com.coverflow.question.infrastructure.AnswerRepository;
import com.coverflow.report.domain.Report;
import com.coverflow.report.domain.ReportType;
import com.coverflow.report.dto.ReportDTO;
import com.coverflow.report.dto.request.FindReportAdminRequest;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.request.UpdateReportRequest;
import com.coverflow.report.dto.response.FindReportResponse;
import com.coverflow.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageDesc;
import static com.coverflow.question.exception.AnswerException.AnswerNotFoundException;
import static com.coverflow.report.domain.ReportType.ANSWER;
import static com.coverflow.report.domain.ReportType.QUESTION;
import static com.coverflow.report.exception.ReportException.ReportNotFoundException;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final AnswerRepository answerRepository;
    private final ReportRepository reportRepository;

    /**
     * [특정 회원의 신고 리스트 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FindReportResponse findMyReport(
            final String memberId,
            final int pageNo,
            final String criterion
    ) {
        Page<Report> reports = reportRepository.findByMemberId(memberId, generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
                .orElseThrow(() -> new ReportNotFoundException(memberId));

        return FindReportResponse.of(
                reports.getTotalPages(),
                reports.getTotalElements(),
                reports.getContent()
                        .stream()
                        .map(ReportDTO::from)
                        .toList()
        );
    }

    /**
     * [관리자 - 신고 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FindReportResponse find(
            final int pageNo,
            final String criterion,
            final FindReportAdminRequest request
    ) {
        Page<Report> reports = reportRepository.findWithFilters(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), request)
                .orElseThrow(() -> new ReportNotFoundException(request));

        return FindReportResponse.of(
                reports.getTotalPages(),
                reports.getTotalElements(),
                reports.getContent()
                        .stream()
                        .map(ReportDTO::from)
                        .toList()
        );
    }

    /**
     * [신고 등록 메서드]
     */
    @Transactional
    public void save(
            final SaveReportRequest request,
            final String memberId
    ) {
        Report report = Report.builder().build();

        if ((QUESTION).equals(ReportType.valueOf(request.type()))) {
            report = new Report(request, memberId);
        }
        if ((ANSWER).equals(ReportType.valueOf(request.type()))) {
            Answer answer = answerRepository.findByIdAndAnswerStatus(request.id(), true)
                    .orElseThrow(() -> new AnswerNotFoundException(request.id()));

            report = new Report(request, answer, memberId);
        }

        reportRepository.save(report);
    }

    /**
     * [관리자 - 신고 수정 메서드]
     */
    @Transactional
    public void update(
            final long reportId,
            final UpdateReportRequest request
    ) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));

        report.updateReport(request);
    }

    /**
     * [관리자 - 신고 삭제 메서드]
     */
    @Transactional
    public void delete(final long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));

        reportRepository.delete(report);
    }
}
