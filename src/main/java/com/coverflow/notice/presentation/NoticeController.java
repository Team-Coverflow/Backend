package com.coverflow.notice.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.global.util.BadWordUtil;
import com.coverflow.notice.application.NoticeService;
import com.coverflow.notice.dto.request.SaveNoticeRequest;
import com.coverflow.notice.dto.request.UpdateNoticeRequest;
import com.coverflow.notice.dto.response.FindNoticeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/notice")
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<ResponseHandler<FindNoticeResponse>> find(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindNoticeResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(noticeService.find(pageNo, criterion))
                        .build());
    }

    @PostMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> save(
            @RequestBody @Valid final SaveNoticeRequest request,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        BadWordUtil.check(request.title());
        BadWordUtil.check(request.content());
        noticeService.save(request, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PatchMapping("/admin/{noticeId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> update(
            @PathVariable @Positive final long noticeId,
            @RequestBody @Valid final UpdateNoticeRequest request
    ) {
        BadWordUtil.check(request.title());
        BadWordUtil.check(request.content());
        noticeService.update(noticeId, request);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/admin/{noticeId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> delete(
            @PathVariable @Positive final long noticeId
    ) {
        noticeService.delete(noticeId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
