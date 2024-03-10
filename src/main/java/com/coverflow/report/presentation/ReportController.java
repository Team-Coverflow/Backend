package com.coverflow.report.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.report.application.ReportService;
import com.coverflow.report.domain.ReportStatus;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.response.FindReportResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    @GetMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReportByMemberId(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.findReportsByMemberId(userDetails.getUsername(), pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReports(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion
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
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @RequestParam @NotBlank final ReportStatus reportStatus
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.findReportsByStatus(pageNo, criterion, reportStatus))
                        .build()
                );
    }

    @PostMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveReport(
            @RequestBody @Valid final SaveReportRequest saveReportRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        reportService.saveReport(saveReportRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @DeleteMapping("/admin/{reportId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteAnswer(
            @PathVariable @Positive final long reportId
    ) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
