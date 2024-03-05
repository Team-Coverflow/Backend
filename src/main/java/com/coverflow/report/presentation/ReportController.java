package com.coverflow.report.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.report.application.ReportService;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.response.FindReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/report")
@RestController
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/report")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReportByMemberId(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.findReportsByMemberId(userDetails.getUsername(), pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin/reports")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReports(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.findReports(pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin/status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReportsByStatus(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion,
            @RequestParam(defaultValue = "등록") @Valid final String status
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.findReportsByStatus(pageNo, criterion, status))
                        .build()
                );
    }

    @PostMapping("/report")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveReport(
            @RequestBody @Valid final SaveReportRequest saveReportRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        reportService.saveReport(saveReportRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    @PutMapping("/admin/report/{reportId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteAnswer(
            @PathVariable @Valid final Long reportId
    ) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }
}
