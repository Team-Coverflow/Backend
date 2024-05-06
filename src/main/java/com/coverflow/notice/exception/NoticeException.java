package com.coverflow.notice.exception;

public class NoticeException extends RuntimeException {

    public NoticeException(final String message) {
        super(message);
    }

    public static class NoticeNotFoundException extends NoticeException {

        public NoticeNotFoundException() {
            super("공지가 존재하지 않습니다.");
        }

        public NoticeNotFoundException(final Object data) {
            super(String.format("공지가 존재하지 않습니다. - request info => %s", data));
        }
    }
}
