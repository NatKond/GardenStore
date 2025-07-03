package de.telran.gardenStore.handler;

import de.telran.gardenStore.dto.AppErrorResponse;
import de.telran.gardenStore.exception.UserWithEmailAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppErrorResponse> handlerException(Exception exception) {

        log.error(exception.getMessage(), exception);

        HttpStatus status =
//                (exception instanceof EntityNotFoundException) ? HttpStatus.NOT_FOUND :
//                (exception instanceof UserWithEmailAlreadyExistsException) ? HttpStatus.CONFLICT :
                        HttpStatus.BAD_REQUEST;

        AppErrorResponse appErrorResponse = AppErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(appErrorResponse, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        AppErrorResponse errorResponse = AppErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWithEmailAlreadyExistsException.class)
    public ResponseEntity<AppErrorResponse> handleUserWithEmailAlreadyExistsException(UserWithEmailAlreadyExistsException exception) {
        log.error(exception.getMessage(), exception);
        AppErrorResponse errorResponse = AppErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exception) {

        log.error(exception.getMessage(), exception);

        Map<String, String> errors = exception.getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.joining(". "))));

        AppErrorResponse appErrorResponseValidation = AppErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .messages(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(appErrorResponseValidation, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        log.error(exception.getMessage(), exception);

        Map<String, String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(". "))));

        AppErrorResponse appErrorResponseValidation = AppErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .messages(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(appErrorResponseValidation, HttpStatus.BAD_REQUEST);
    }
}

