package com.coverflow.report.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.report.application.ReportService;
import com.coverflow.report.dto.response.FindReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/report")
@RestController
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/find-report/{memberId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReportByMemberId(
            @PathVariable @Valid final UUID memberId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회원의 신고 조회에 성공했습니다.")
                        .data(reportService.findReportsByMemberId(memberId))
                        .build());
    }

    @GetMapping("/find-reports")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReports(
            @PathVariable @Valid final UUID memberId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회원의 신고 조회에 성공했습니다.")
                        .data(reportService.findReports())
                        .build());
    }
}
