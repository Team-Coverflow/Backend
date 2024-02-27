package com.coverflow.question.exception;

public class AnswerException extends RuntimeException {

    public AnswerException(final String message) {
        super(message);
    }

    public static class AnswerNotFoundException extends AnswerException {

        public AnswerNotFoundException(final long questionId) {
            super(String.format("답변이 존재하지 않습니다. - request info { id : %d }", questionId));
        }

        public AnswerNotFoundException(final String string) {
            super(String.format("답변이 존재하지 않습니다. - request info { string : %s }", string));
        }

        public AnswerNotFoundException() {
            super("답변이 존재하지 않습니다.");
        }
    }

    public static class AnswerExistException extends AnswerException {

        public AnswerExistException(final long id) {
            super(String.format("이미 답변이 존재합니다. - request info { id : %d }", id));
        }
    }
}
