package com.coverflow.member.exception;

import java.util.UUID;

public class MemberException extends RuntimeException {
    public MemberException(final String message) {
        super(message);
    }

    public static class MemberNotFoundException extends MemberException {

        public MemberNotFoundException(final UUID memberId) {
            super(String.format("회원이 존재하지 않습니다. - request info { memberId : %s }", memberId));
        }

        public MemberNotFoundException(final String username) {
            super(String.format("회원이 존재하지 않습니다. - request info { string : %s }", username));
        }
    }

    public static class AllMemberNotFoundException extends MemberException {

        public AllMemberNotFoundException() {
            super("모든 회원이 존재하지 않습니다.");
        }
    }

    public static class NotEnoughCurrencyException extends MemberException {

        public NotEnoughCurrencyException() {
            super("화폐가 부족합니다.");
        }
    }
}
