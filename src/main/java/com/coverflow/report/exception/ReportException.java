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
}
