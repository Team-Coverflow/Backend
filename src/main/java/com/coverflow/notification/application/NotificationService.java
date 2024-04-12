package com.coverflow.notification.application;

import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.dto.request.UpdateNotificationRequest;
import com.coverflow.notification.dto.response.FindNotificationResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {

    /**
     * 실시간 알림 연결
     */
    SseEmitter connect(final String memberId, final String lastEventId);

    /**
     * 실시간 알림 보내기
     */
    void send(final Notification notification);

    /**
     * 알림 생성
     */
    void save(final Notification notification);

    /**
     * 알림 조회
     */
    FindNotificationResponse find(final String memberId);

    /**
     * 알림 수정
     */
    void update(final List<UpdateNotificationRequest> request);
}
