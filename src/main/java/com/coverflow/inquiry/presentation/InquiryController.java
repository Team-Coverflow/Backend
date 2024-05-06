package com.coverflow.inquiry.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.global.util.BadWordUtil;
import com.coverflow.inquiry.application.InquiryService;
import com.coverflow.inquiry.dto.request.FindInquiryAdminRequest;
import com.coverflow.inquiry.dto.request.SaveInquiryRequest;
import com.coverflow.inquiry.dto.request.UpdateInquiryRequest;
import com.coverflow.inquiry.dto.response.FindAllInquiriesResponse;
import com.coverflow.inquiry.dto.response.FindInquiryResponse;
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
@RequestMapping("/api/inquiry")
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/me")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindInquiryResponse>> findMyInquiries(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindInquiryResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(inquiryService.findMyInquiries(pageNo, criterion, userDetails.getUsername()))
                        .build());
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<FindAllInquiriesResponse>> find(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @ModelAttribute final FindInquiryAdminRequest request
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindAllInquiriesResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(inquiryService.find(pageNo, criterion, request))
                        .build());
    }

    @PostMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> save(
            @RequestBody @Valid final SaveInquiryRequest request,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        BadWordUtil.check(request.title());
        BadWordUtil.check(request.content());
        inquiryService.save(request, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PatchMapping("/admin/{inquiryId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> update(
            @PathVariable @Positive final long inquiryId,
            @RequestBody @Valid final UpdateInquiryRequest request
    ) {
        BadWordUtil.check(request.inquiryAnswer());
        inquiryService.update(inquiryId, request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/admin/{inquiryId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> delete(
            @PathVariable @Positive final long inquiryId
    ) {
        inquiryService.delete(inquiryId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
