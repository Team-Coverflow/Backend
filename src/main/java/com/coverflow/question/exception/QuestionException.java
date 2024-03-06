package com.coverflow.question.exception;

public class QuestionException extends RuntimeException {
    public QuestionException(final String message) {
        super(message);
    }

    public static class QuestionNotFoundException extends QuestionException {

        public QuestionNotFoundException(final long questionId) {
            super(String.format("질문 글이 존재하지 않습니다. - request info { id : %d }", questionId));
        }

        public QuestionNotFoundException(final String string) {
            super(String.format("질문 글이 존재하지 않습니다. - request info { string : %s }", string));
        }

        public QuestionNotFoundException() {
            super("질문 글이 존재하지 않습니다.");
        }
    }

    public static class QuestionExistException extends QuestionException {

        public QuestionExistException(final long id) {
            super(String.format("이미 질문 글이 존재합니다. - request info { id : %d }", id));
        }
    }
}
