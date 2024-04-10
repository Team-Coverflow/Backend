package com.coverflow.notification.dto.response;

import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.domain.NotificationType;

import java.time.LocalDate;

public record SendNotificationResponse(
        String eventId,
        String content,
        String uri,
        NotificationType type,
        boolean isRead,
        LocalDate createdAt
) {
    public static SendNotificationResponse of(final String eventId, final Notification notification) {
        return new SendNotificationResponse(
                eventId,
                notification.getContent(),
                notification.getUri(),
                notification.getType(),
                notification.isRead(),
                LocalDate.from(notification.getCreatedAt())
        );
    }
}
