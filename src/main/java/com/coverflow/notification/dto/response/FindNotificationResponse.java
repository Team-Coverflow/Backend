package com.coverflow.notification.dto.response;

import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.domain.NotificationType;

import java.time.LocalDateTime;

public record FindNotificationResponse(
        String content,
        NotificationType type,
        LocalDateTime createdAt
) {

    public static FindNotificationResponse from(Notification notification) {
        return new FindNotificationResponse(
                notification.getContent(),
                notification.getType(),
                notification.getCreatedAt()
        );
    }
}
