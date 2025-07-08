package de.telran.gardenStore.handler;

import de.telran.gardenStore.dto.ApiErrorResponse;
import de.telran.gardenStore.exception.EntityAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
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
    public ResponseEntity<ApiErrorResponse> handlerException(Exception exception) {

        log.error(exception.getMessage(), exception);

        HttpStatus status =
//                (exception instanceof EntityNotFoundException) ? HttpStatus.NOT_FOUND :
//                (exception instanceof UserWithEmailAlreadyExistsException) ? HttpStatus.CONFLICT :
                        HttpStatus.BAD_REQUEST;

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorResponse, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {

        log.error(exception.getMessage(), exception);

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ApiErrorResponse> handleMappingException(MappingException exception) {

        Throwable rootCause = exception.getCause();
        if (rootCause instanceof EntityNotFoundException) {
            return handleEntityNotFoundException((EntityNotFoundException) rootCause);
        }

        log.error(exception.getMessage(), exception);

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .exception(rootCause.getClass().getSimpleName())
                    .message(rootCause.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(LocalDateTime.now())
                    .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserWithEmailAlreadyExistsException(RuntimeException exception) {

        log.error(exception.getMessage(), exception);

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
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

        ApiErrorResponse apiErrorResponseValidation = ApiErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .messages(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorResponseValidation, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        log.error(exception.getMessage(), exception);

        Map<String, String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(". "))));

        ApiErrorResponse apiErrorResponseValidation = ApiErrorResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .messages(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorResponseValidation, HttpStatus.BAD_REQUEST);
    }
}

