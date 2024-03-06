package com.coverflow.Inquiry.exception;

import java.util.UUID;

public class InquiryException extends RuntimeException {

    public InquiryException(final String message) {
        super(message);
    }

    public static class InquiryNotFoundException extends InquiryException {

        public InquiryNotFoundException(final UUID memberId) {
            super(String.format("문의가 존재하지 않습니다. - request info { memberId : %s }", memberId));
        }

        public InquiryNotFoundException(final Long enquiryId) {
            super(String.format("문의가 존재하지 않습니다. - request info { enquiryId : %d }", enquiryId));
        }

        public InquiryNotFoundException(final String string) {
            super(String.format("문의가 존재하지 않습니다. - request info { string : %s }", string));
        }

        public InquiryNotFoundException() {
            super("문의가 존재하지 않습니다.");
        }
    }
}
