package com.coverflow.visitor.exception;

import java.time.LocalDateTime;

public class VisitorException extends RuntimeException {

    public VisitorException(final String message) {
        super(message);
    }

    public static class DayNotFoundException extends VisitorException {

        public DayNotFoundException(final LocalDateTime today) {
            super(String.format("오늘 날짜가 존재하지 않습니다. - request info { username : %s }", today));
        }
    }
}
