package com.coverflow.report.exception;

public class ReportException extends RuntimeException {

    public ReportException(final String message) {
        super(message);
    }

    public static class ReportNotFoundException extends ReportException {

        public ReportNotFoundException() {
            super("신고가 존재하지 않습니다.");
        }

        public ReportNotFoundException(final Object data) {
            super(String.format("신고가 존재하지 않습니다. - request info => %s", data));
        }
    }

    public static class ReportExistException extends ReportException {

        public ReportExistException(final Object data) {
            super(String.format("해당 회원은 이미 해당 게시물에 대한 신고를 했습니다. - request info => %s", data));
        }
    }
}
