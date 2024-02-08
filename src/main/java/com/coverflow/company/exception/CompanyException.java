package com.coverflow.company.exception;

public class CompanyException extends RuntimeException {

    public CompanyException(final String message) {
        super(message);
    }

    public static class CompanyNotFoundException extends CompanyException {

        public CompanyNotFoundException(final String name) {
            super(String.format("회사가 존재하지 않습니다. - request info { name : %s }", name));
        }

        public CompanyNotFoundException() {
            super("회사가 존재하지 않습니다.");
        }
    }

    public static class CompanyExistException extends CompanyException {

        public CompanyExistException(final String name) {
            super(String.format("이미 회사가 존재합니다. - request info { name : %s }", name));
        }
    }
}
