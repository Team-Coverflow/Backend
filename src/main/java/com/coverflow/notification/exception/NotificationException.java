package com.coverflow.notification.exception;

public class NotificationException extends RuntimeException {

    public NotificationException(final String message) {
        super(message);
    }

    public static class NotificationNotFoundException extends NotificationException {

        public NotificationNotFoundException() {
            super("알림이 존재하지 않습니다.");
        }

        public NotificationNotFoundException(final Long notificationId) {
            super(String.format("알림이 존재하지 않습니다. - request info { companyId : %d }", notificationId));
        }

        public NotificationNotFoundException(final String string) {
            super(String.format("알림이 존재하지 않습니다. - request info { string : %s }", string));
        }
    }
}
