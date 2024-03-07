package com.coverflow.notification.dto.request;

import com.coverflow.notification.domain.NotificationStatus;

public record UpdateNotificationRequest(
        long notificationId,
        NotificationStatus notificationStatus
) {
}
