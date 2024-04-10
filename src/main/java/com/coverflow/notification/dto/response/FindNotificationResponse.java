package com.coverflow.notification.dto.response;

import com.coverflow.notification.dto.NotificationDTO;

import java.util.List;

public record FindNotificationResponse(
        int noReadElements,
        List<NotificationDTO> notificationList
) {

    public static FindNotificationResponse of(
            int noReadElements,
            final List<NotificationDTO> notificationList
    ) {
        return new FindNotificationResponse(noReadElements, notificationList);
    }
}
