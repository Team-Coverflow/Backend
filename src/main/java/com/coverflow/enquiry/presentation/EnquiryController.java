package com.coverflow.enquiry.presentation;

import com.coverflow.enquiry.application.EnquiryService;
import com.coverflow.enquiry.dto.request.SaveEnquiryRequest;
import com.coverflow.enquiry.dto.response.FindAllEnquiriesResponse;
import com.coverflow.enquiry.dto.response.FindEnquiryResponse;
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
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/enquiry")
@RestController
public class EnquiryController {

    private final EnquiryService enquiryService;

    @GetMapping("/find-enquiry")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindEnquiryResponse>>> findEnquiryByMemberId(
            @RequestParam(defaultValue = "0", value = "pageNo") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt", value = "criterion") @Valid final String criterion,
            @RequestParam("memberId") @Valid final UUID memberId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindEnquiryResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회원의 문의 조회에 성공했습니다.")
                        .data(enquiryService.findEnquiryByMemberId(pageNo, criterion, memberId))
                        .build());
    }

    @GetMapping("/admin/find-enquiries")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllEnquiriesResponse>>> findEnquiries(
            @RequestParam(defaultValue = "0", value = "pageNo") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt", value = "criterion") @Valid final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllEnquiriesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("전체 문의 조회에 성공했습니다.")
                        .data(enquiryService.findEnquiries(pageNo, criterion))
                        .build());
    }

    @GetMapping("/admin/find-by-status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllEnquiriesResponse>>> findEnquiriesByStatus(
            @RequestParam(defaultValue = "0", value = "pageNo") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt", value = "criterion") @Valid final String criterion,
            @RequestParam(defaultValue = "등록", value = "status") @Valid final String status
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllEnquiriesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 상태의 문의 검색에 성공했습니다.")
                        .data(enquiryService.findEnquiriesByStatus(pageNo, criterion, status))
                        .build()
                );
    }

    @PostMapping("/save-enquiry")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveEnquiry(
            @RequestBody @Valid final SaveEnquiryRequest saveEnquiryRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        enquiryService.saveEnquiry(saveEnquiryRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("문의 등록에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/delete-enquiry/{enquiryId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteAnswer(
            @PathVariable @Valid final Long enquiryId
    ) {
        enquiryService.deleteEnquiry(enquiryId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("문의 삭제에 성공했습니다.")
                        .build());
    }
}
