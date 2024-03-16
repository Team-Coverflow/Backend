package com.coverflow.feedback.exception;

public class FeedbackException extends RuntimeException {

    public FeedbackException(final String message) {
        super(message);
    }

    public static class FeedbackNotFoundException extends FeedbackException {

        public FeedbackNotFoundException() {
            super("피드백이 존재하지 않습니다.");
        }

        public FeedbackNotFoundException(final Object data) {
            super(String.format("피드백이 존재하지 않습니다. - request info => %s", data));
        }
    }
}
