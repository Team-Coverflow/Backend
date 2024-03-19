package com.coverflow.global.exception;

public class GlobalException extends RuntimeException {

    public GlobalException(final String message) {
        super(message);
    }

    public static class ExistBadwordException extends GlobalException {

        public ExistBadwordException() {
            super("비속어가 존재합니다.");
        }
    }
}
