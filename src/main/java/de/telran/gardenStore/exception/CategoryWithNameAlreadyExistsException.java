package de.telran.gardenStore.exception;

public class CategoryWithNameAlreadyExistsException extends RuntimeException {
    public CategoryWithNameAlreadyExistsException(String message) {
        super(message);
    }
}
