package com.coverflow.Inquiry.presentation;

import com.coverflow.Inquiry.application.InquiryService;
import com.coverflow.Inquiry.dto.request.SaveInquiryRequest;
import com.coverflow.Inquiry.dto.request.UpdateInquiryRequest;
import com.coverflow.Inquiry.dto.response.FindAllInquiriesResponse;
import com.coverflow.Inquiry.dto.response.FindInquiryResponse;
import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiry")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindInquiryResponse>>> findInquiryByMemberId(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindInquiryResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(inquiryService.findInquiryByMemberId(pageNo, criterion, userDetails.getUsername()))
                        .build());
    }

    @GetMapping("/admin/inquiries")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllInquiriesResponse>>> findInquiries(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllInquiriesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(inquiryService.findInquiries(pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin/status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllInquiriesResponse>>> findInquiriesByStatus(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion,
            @RequestParam(defaultValue = "답변대기") @Valid final String status
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllInquiriesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(inquiryService.findInquiriesByStatus(pageNo, criterion, status))
                        .build()
                );
    }

    @PostMapping("/inquiry")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveInquiry(
            @RequestBody @Valid final SaveInquiryRequest saveInquiryRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        inquiryService.saveInquiry(saveInquiryRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    @PutMapping("/inquiry")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateInquiry(
            @RequestBody @Valid final UpdateInquiryRequest updateInquiryRequest
    ) {
        inquiryService.updateInquiry(updateInquiryRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    /**
     * TODO 사용자가 삭제할 지 or 관리자가 삭제할 지 고민 필요
     */
    @PutMapping("/admin/inquiry/{inquiryId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteInquiry(
            @PathVariable @Valid final long inquiryId
    ) {
        inquiryService.deleteInquiry(inquiryId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }
}
