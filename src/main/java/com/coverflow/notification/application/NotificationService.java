package com.coverflow.notification.application;

import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.dto.request.UpdateNotificationRequest;
import com.coverflow.notification.dto.response.FindNotificationResponse;
import com.coverflow.notification.exception.NotificationException;
import com.coverflow.notification.infrastructure.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * [보낼 메시지 담는 메서드]
     */
    public SseEmitter add(SseEmitter emitter) {
        emitters.add(emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        log.info("emitter list: {}", emitters);
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    public void alert() {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("alert")
                        .data("count"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * [알림 조회 메서드]
     */
    public List<FindNotificationResponse> findNotification(String memberId) {
        final List<Notification> notifications = notificationRepository.findByMemberId(UUID.fromString(memberId))
                .orElseThrow(() -> new NotificationException.NotificationNotFoundException(memberId));
        final List<FindNotificationResponse> findNotifications = new ArrayList<>();

        for (int i = 0; i < notifications.size(); i++) {
            findNotifications.add(i, FindNotificationResponse.from(notifications.get(i)));
        }
        return findNotifications;
    }

    /**
     * [알림 수정 메서드]
     */
    @Transactional
    public void updateNotification(final List<UpdateNotificationRequest> request) {
        for (UpdateNotificationRequest updateNotificationRequest : request) {
            final Notification notification = notificationRepository.findById(updateNotificationRequest.notificationId())
                    .orElseThrow(() -> new NotificationException.NotificationNotFoundException(updateNotificationRequest.notificationId()));

            notification.updateStatus(updateNotificationRequest.status());
        }
    }

    /**
     * [알림 삭제 메서드]
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteNotification() {
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteByCreatedAt(date);
    }

}
