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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.coverflow.report.domain.ReportType.ANSWER;
import static com.coverflow.report.domain.ReportType.QUESTION;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReportService {

    private final AnswerRepository answerRepository;
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

    /**
     * [신고 등록 메서드]
     */
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
}
