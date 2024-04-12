package com.coverflow.notification.dto;

import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String content;
    private String uri;
    private NotificationType type;
    private boolean isRead;
    private LocalDate createdAt;

    public static NotificationDTO from(final Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getContent(),
                notification.getUri(),
                notification.getType(),
                notification.isRead(),
                LocalDate.from(notification.getCreatedAt())
        );
    }
}
