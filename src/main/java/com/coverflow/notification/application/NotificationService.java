package com.coverflow.notification.application;

import com.coverflow.notification.domain.Notification;

public interface NotificationService {

    void save(final Notification notification);

    void send(final Notification notification);
}
