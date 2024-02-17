package com.coverflow.notification.dto.request;

public record UpdateNotificationRequest(
        Long notificationId,
        String status
) {
}
