package ru.practicum.main.service.exception;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> errorMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.error("EH: MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> errorMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        log.error("EH: MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(ex.getMessage() + ". Param:" + ex.getName() + " Value=" + ex.getValue())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(final NotFoundException ex) {
        log.error("EH: NotFoundException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(ex.getMessage())
                .reason("The required object was not found.")
                .httpStatus(HttpStatus.NOT_FOUND)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiError> errorDataIntegrityViolationException(final DataIntegrityViolationException ex){
//        log.error("EH: DataIntegrityViolationException: {}", ex.getMessage(), ex);
//        ApiError apiError = ApiError.builder()
//                .errors(Arrays.stream(ex.getStackTrace())
//                        .map(StackTraceElement::toString)
//                        .collect(Collectors.toList()))
//                .message(Objects.requireNonNull(ex.getRootCause()).getMessage())
//                .reason("Integrity constraint has been violated.")
//                .httpStatus(HttpStatus.CONFLICT)
//                .timeStamp(now())
//                .build();
//        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> errorConstraintViolationException(final ConstraintViolationException ex) {
        log.error("EH: ConstraintViolationException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(ex.getCause().getMessage())
                .reason("Integrity constraint has been violated.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }


//    @ExceptionHandler(IllegalArgumentException.class)
////    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ApiError> errorIllegalArgumentException(final IllegalArgumentException e) {
//        log.error("IllegalArgumentException: {}", e.getMessage(), e);
//
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse errorThrowableException(final MethodArgumentNotValidException e) {
//        log.error("MethodArgumentNotValidException. {}", e.getMessage(), e);
//
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<String> errorResponseStatusException(final ResponseStatusException e) {
//        log.error("ResponseStatusException. {}", e.getReason(), e);
//        return new ResponseEntity<>(e.getReason(), e.getStatus());
//    }
//
//    @ExceptionHandler(Throwable.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse errorThrowableException(final Throwable e) {
//        log.error("Internal Server Error. {}", e.getMessage(), e);
//
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
}
