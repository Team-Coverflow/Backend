package com.coverflow.notification.presentation;

import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.dto.request.UpdateNotificationRequest;
import com.coverflow.notification.dto.response.FindNotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/find-notification")
    public ResponseEntity<ResponseHandler<List<FindNotificationResponse>>> findNotification(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindNotificationResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("알림 조회에 성공했습니다.")
                        .data(notificationService.findNotification(userDetails.getUsername()))
                        .build()
                );
    }

    @PostMapping("/update-notification")
    public ResponseEntity<ResponseHandler<Void>> updateNotification(
            @RequestBody final List<UpdateNotificationRequest> requests
    ) {
        notificationService.updateNotification(requests);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("알림 변경에 성공했습니다.")
                        .build()
                );
    }
}
