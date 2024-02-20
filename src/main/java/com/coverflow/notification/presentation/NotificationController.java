package com.coverflow.notification.presentation;

import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.notification.application.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    //    private final SseEmitter sseEmitters;
    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<ResponseHandler<SseEmitter>> connect() {
        final SseEmitter emitter = new SseEmitter();
        
        notificationService.add(emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .body(ResponseHandler.<SseEmitter>builder()
                        .statusCode(HttpStatus.OK)
                        .message("알림 서버 연결 성공했습니다.")
                        .data(emitter)
                        .build()
                );
    }

    @PostMapping("/count")
    public ResponseEntity<Void> count() {
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/find-notification")
//    public ResponseEntity<ResponseHandler<List<FindNotificationResponse>>> findNotification(
//            @AuthenticationPrincipal final UserDetails userDetails
//    ) {
//        return ResponseEntity.ok()
//                .body(ResponseHandler.<List<FindNotificationResponse>>builder()
//                        .statusCode(HttpStatus.OK)
//                        .message("알림 조회에 성공했습니다.")
//                        .data(notificationService.findNotification(userDetails.getUsername()))
//                        .build()
//                );
//    }
//
//    @PostMapping("/update-notification")
//    public ResponseEntity<ResponseHandler<Void>> updateNotification(
//            @RequestBody final List<UpdateNotificationRequest> requests
//    ) {
//        notificationService.updateNotification(requests);
//        return ResponseEntity.ok()
//                .body(ResponseHandler.<Void>builder()
//                        .statusCode(HttpStatus.OK)
//                        .message("알림 변경에 성공했습니다.")
//                        .build()
//                );
//    }
}