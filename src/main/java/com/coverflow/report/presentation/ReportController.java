package com.coverflow.report.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.global.util.BadwordUtil;
import com.coverflow.report.application.ReportService;
import com.coverflow.report.domain.ReportStatus;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.request.UpdateReportRequest;
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

@RequiredArgsConstructor
@RequestMapping("/api/report")
@RestController
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/me")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindReportResponse>> findMyReport(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindReportResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.findMyReport(userDetails.getUsername(), pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindReportResponse>> find(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindReportResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.find(pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin/status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindReportResponse>> findByStatus(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @RequestParam @NotBlank final ReportStatus reportStatus
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindReportResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(reportService.findByStatus(pageNo, criterion, reportStatus))
                        .build()
                );
    }

    @PostMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> save(
            @RequestBody @Valid final SaveReportRequest request,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        BadwordUtil.check(request.content());
        reportService.save(request, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PatchMapping("/admin/{reportId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> update(
            @PathVariable @Positive final long reportId,
            @RequestBody @Valid final UpdateReportRequest request
    ) {
        reportService.update(reportId, request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/admin/{reportId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> delete(
            @PathVariable @Positive final long reportId
    ) {
        reportService.delete(reportId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
