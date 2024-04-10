package com.coverflow.notification.application;

import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.dto.NotificationDTO;
import com.coverflow.notification.dto.request.UpdateNotificationRequest;
import com.coverflow.notification.dto.response.FindNotificationResponse;
import com.coverflow.notification.exception.NotificationException;
import com.coverflow.notification.infrastructure.EmitterRepository;
import com.coverflow.notification.infrastructure.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    /**
     * [알림 서버 연결 메서드]
     * 알림 서버 접속 시 요청 회원의 고유 이벤트 id를 key, SseEmitter 인스턴스를 value로
     * 알림 서버 저장소에 추가합니다.
     */
    public SseEmitter connect(
            final String memberId,
            final String lastEventId
    ) {
        // 매 연결마다 고유 이벤트 id 부여
        String eventId = memberId + "_" + System.currentTimeMillis();

        // SseEmitter 인스턴스 생성 후 Map에 저장
        SseEmitter emitter = emitterRepository.save(eventId, new SseEmitter(DEFAULT_TIMEOUT));

        // 이벤트 전송 시
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            emitterRepository.delete(eventId);
        });

        // 이벤트 스트림 연결 끊길 시
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
            emitterRepository.delete(eventId);
        });

        // 첫 연결 시 503 Service Unavailable 방지용 더미 Event 전송
        sendToClient(eventId, emitter, "알림 서버 연결 성공. [memberId = " + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 모두 전송
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(memberId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(entry.getKey(), emitter, entry.getValue()));
        }

        return emitter;
    }

    /**
     * [알림 전송 메서드]
     */
    @Async
    public void send(final Notification notification) {
        notificationRepository.save(notification);

        String receiverId = notification.getMember().getId().toString();
        String eventId = notification.getMember().getId() + "_" + System.currentTimeMillis();

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEventStartWithId(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(eventId, emitter, notification);
                }
        );
    }

    /**
     * [직접 이벤트를 클라이언트로 전송하는 메서드]
     */
    private void sendToClient(
            final String eventId,
            final SseEmitter emitter,
            final Object object
    ) {
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .id(eventId)
                    .data(object)
            );
        } catch (IOException e) {
            emitterRepository.delete(eventId);
            throw new RuntimeException("알림 서버 연결 오류");
        }
    }

    /**
     * [알림 조회 메서드]
     */
    @Transactional(readOnly = true)
    public FindNotificationResponse find(final String memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(UUID.fromString(memberId), LocalDateTime.now().minusDays(31))
                .orElseThrow(() -> new NotificationException.NotificationNotFoundException(memberId));

        return FindNotificationResponse.of(
                countNoReadNotification(notifications),
                notifications.stream().map(NotificationDTO::from).toList()
        );
    }

    private int countNoReadNotification(final List<Notification> notifications) {
        int count = 0;

        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                count++;
            }
        }
        return count;
    }

    /**
     * [알림 수정 메서드]
     * 사용자가 알림 읽으면 상태 true로 변경
     */
    @Transactional
    public void update(final List<UpdateNotificationRequest> request) {
        for (UpdateNotificationRequest updateNotificationRequest : request) {
            final Notification notification = notificationRepository.findById(updateNotificationRequest.notificationId())
                    .orElseThrow(() -> new NotificationException.NotificationNotFoundException(updateNotificationRequest.notificationId()));

            notification.updateIsRead(true);
        }
    }

    /**
     * [알림 삭제 메서드]
     * 30일이 지난 알림들 매일 자정에 삭제
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void delete() {
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteByCreatedAt(date);
    }
}
