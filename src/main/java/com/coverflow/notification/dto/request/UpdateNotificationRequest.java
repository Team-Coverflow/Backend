package com.coverflow.notification.dto.request;

import com.coverflow.notification.domain.NotificationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateNotificationRequest(
        @Positive
        long notificationId,
        @NotBlank
        NotificationStatus notificationStatus
) {
}
