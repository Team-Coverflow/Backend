package com.coverflow.global.exception;

public class GlobalException extends RuntimeException {

    public GlobalException(final String message) {
        super(message);
    }

    public static class LogoutMemberException extends GlobalException {

        public LogoutMemberException() {
            super("이미 로그아웃을 했던 회원입니다.");
        }
    }

    public static class JWTNotFoundException extends GlobalException {

        public JWTNotFoundException() {
            super("토큰이 비어있습니다.");
        }
    }

    public static class ExistBadwordException extends GlobalException {

        public ExistBadwordException() {
            super("비속어가 존재합니다.");
        }
    }
}
