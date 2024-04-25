package com.coverflow.notification.presentation;

import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.dto.request.UpdateNotificationRequest;
import com.coverflow.notification.dto.response.FindNotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @MemberAuthorize
    public ResponseEntity<SseEmitter> connect(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") final String lastEventId,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok(notificationService.connect(userDetails.getUsername(), lastEventId));
    }

    @GetMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<FindNotificationResponse>> find(
            @RequestParam long lastIndex,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindNotificationResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(notificationService.find(lastIndex, userDetails.getUsername()))
                        .build()
                );
    }

    @PatchMapping
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> update(
            @RequestBody @Valid final List<UpdateNotificationRequest> requests
    ) {
        notificationService.update(requests);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build()
                );
    }
}