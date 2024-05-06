package com.coverflow.question.exception;

public class QuestionException extends RuntimeException {

    public QuestionException(final String message) {
        super(message);
    }

    public static class QuestionNotFoundException extends QuestionException {

        public QuestionNotFoundException() {
            super("질문이 존재하지 않습니다.");
        }

        public QuestionNotFoundException(final Object data) {
            super(String.format("질문이 존재하지 않습니다. - request info => %s", data));
        }
    }

    public static class QuestionExistException extends QuestionException {

        public QuestionExistException(final long id) {
            super(String.format("이미 질문이 존재합니다. - request info { id : %d }", id));
        }
    }

    public static class AlreadySelectedQuestionException extends QuestionException {

        public AlreadySelectedQuestionException(final Object data) {
            super(String.format("이미 채택된 답변이 있는 질문입니다. - request info => %s", data));
        }
    }
}
