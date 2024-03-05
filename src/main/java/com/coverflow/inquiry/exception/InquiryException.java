package com.coverflow.inquiry.exception;

public class InquiryException extends RuntimeException {

    public InquiryException(final String message) {
        super(message);
    }

    public static class InquiryNotFoundException extends InquiryException {

        public InquiryNotFoundException() {
            super("문의가 존재하지 않습니다.");
        }

        public InquiryNotFoundException(final Object data) {
            super(String.format("문의가 존재하지 않습니다. - request info => %s", data));
        }

    }
}
