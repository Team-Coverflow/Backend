package com.coverflow.global.handler;

import com.coverflow.company.exception.CompanyException;
import com.coverflow.member.exception.MemberException;
import com.coverflow.visitor.exception.VisitorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.DateTimeException;
import java.util.Random;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "관리자에게 문의해 주세요.";
    private static final String DEFAULT_FORMAT_ERROR_MESSAGE = "잘못된 형식입니다.";
    private static final String ERROR_KEY_FORMAT = "%n error key : %s";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int ERROR_KEY_LENGTH = 5;
    private static final String EXCEPTION_CLASS_TYPE_MESSAGE_FORMANT = "%n class type : %s";
    private final Random random = new Random();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final String defaultErrorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn(defaultErrorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(defaultErrorMessage));
    }

    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            DateTimeException.class
    })
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(final DateTimeException exception) {
        log.warn(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("DateTime 형식이 잘못되었습니다. 서버 관리자에게 문의해 주세요."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        log.warn(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(DEFAULT_FORMAT_ERROR_MESSAGE));
    }

    @ExceptionHandler(value = {
            MemberException.MemberNotFoundException.class,
            CompanyException.CompanyNotFoundException.class,
            VisitorException.DayNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(final RuntimeException exception) {
        final String message = exception.getMessage();
        log.warn(message);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(message));
    }

//    @ExceptionHandler()
//    public ResponseEntity<ErrorResponse> handleCustomBadRequestException(final RuntimeException exception) {
//        final String message = exception.getMessage();
//        log.warn(message);
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorResponse(message));
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException exception) {
        final String message = exception.getMessage();
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ERROR_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        final String errorKeyInfo = String.format(ERROR_KEY_FORMAT, sb);
        final String exceptionTypeInfo = String.format(EXCEPTION_CLASS_TYPE_MESSAGE_FORMANT, exception.getClass());
        log.error(message + errorKeyInfo + exceptionTypeInfo);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(DEFAULT_ERROR_MESSAGE + errorKeyInfo));
    }
}
