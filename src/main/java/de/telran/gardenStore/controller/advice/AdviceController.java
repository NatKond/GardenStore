package de.telran.gardenStore.controller.advice;

import de.telran.gardenStore.exception.CategoryNotFoundException;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class AdviceController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public String handlerException(Exception exception) {
        exception.printStackTrace();
        return "Sorry, an error has occurred : " + exception.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(exception = {CategoryNotFoundException.class, UserNotFoundException.class, ProductNotFoundException.class, FavoriteNotFoundException.class})
    public String handlerException(RuntimeException exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exception) {
        Map<String, ?> errors = exception.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, ?> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
