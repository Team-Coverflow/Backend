package com.coverflow.notification.dto.request;

import jakarta.validation.constraints.Positive;

public record UpdateNotificationRequest(
        @Positive
        long notificationId
) {
}
