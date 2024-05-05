package com.coverflow.question.exception;

public class AnswerException extends RuntimeException {

    public AnswerException(final String message) {
        super(message);
    }

    public static class AnswerNotFoundException extends AnswerException {

        public AnswerNotFoundException() {
            super("답변이 존재하지 않습니다.");
        }

        public AnswerNotFoundException(final Object data) {
            super(String.format("답변이 존재하지 않습니다. - request info => %s", data));
        }
    }

    public static class AnswerExistException extends AnswerException {

        public AnswerExistException(final Object data) {
            super(String.format("이미 답변이 존재합니다. - request info => %s", data));
        }
    }

    public static class QuestionAuthorException extends AnswerException {

        public QuestionAuthorException(final Object data) {
            super(String.format("질문 작성자는 답변 작성이 불가능합니다. - request info => %s", data));
        }
    }

    public static class OtherSelectionException extends AnswerException {

        public OtherSelectionException(final Object data) {
            super(String.format("질문 작성자 이외엔 채택이 불가능합니다. - request info => %s", data));
        }
    }

    public static class AlreadySelectedQuestionException extends AnswerException {

        public AlreadySelectedQuestionException(final Object data) {
            super(String.format("이미 채택된 답변입니다. - request info => %s", data));
        }
    }
}
