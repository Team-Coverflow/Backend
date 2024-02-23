package com.coverflow.enquiry.exception;

import java.util.UUID;

public class EnquiryException extends RuntimeException {

    public EnquiryException(final String message) {
        super(message);
    }

    public static class EnquiryNotFoundException extends EnquiryException {

        public EnquiryNotFoundException(final UUID memberId) {
            super(String.format("문의가 존재하지 않습니다. - request info { id : %s }", memberId));
        }

        public EnquiryNotFoundException(final Long enquiryId) {
            super(String.format("문의가 존재하지 않습니다. - request info { id : %d }", enquiryId));
        }

        public EnquiryNotFoundException() {
            super("문의가 존재하지 않습니다.");
        }
    }
}
