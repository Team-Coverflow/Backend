package com.coverflow.notification.dto.request;

public record UpdateNotificationRequest(
        long notificationId,
        String status
) {
}
