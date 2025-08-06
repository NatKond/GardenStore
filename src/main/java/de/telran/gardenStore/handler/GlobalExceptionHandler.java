package de.telran.gardenStore.handler;

import de.telran.gardenStore.dto.ApiResponse;
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

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlerException(Exception exception) {
        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(
                ApiResponse.error(exception, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(
                ApiResponse.error(exception, HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ApiResponse> handleMappingException(MappingException exception) {
        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(
                ApiResponse.error(exception, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUserWithEmailAlreadyExistsException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(
                ApiResponse.error(exception, HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);
        Map<String, String> messages = exception.getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.joining(". "))));

        return new ResponseEntity<>(
                ApiResponse.error(exception, messages, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        Map<String, String> messages = exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(". "))));

        return new ResponseEntity<>(
                ApiResponse.error(exception, messages, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }
}

