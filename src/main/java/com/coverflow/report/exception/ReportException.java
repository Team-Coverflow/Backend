package com.coverflow.report.exception;

import java.util.UUID;

public class ReportException extends RuntimeException {
    public ReportException(final String message) {
        super(message);
    }

    public static class ReportNotFoundException extends ReportException {

        public ReportNotFoundException(final UUID memberId) {
            super(String.format("신고가 존재하지 않습니다. - request info { memberId : %s }", memberId));
        }

        public ReportNotFoundException(final Long reportId) {
            super(String.format("신고가 존재하지 않습니다. - request info { reportId : %d }", reportId));
        }

        public ReportNotFoundException(final String string) {
            super(String.format("신고가 존재하지 않습니다. - request info { string : %s }", string));
        }

        public ReportNotFoundException() {
            super("신고가 존재하지 않습니다.");
        }
    }
}
