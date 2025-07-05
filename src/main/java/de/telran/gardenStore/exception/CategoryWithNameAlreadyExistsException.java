package de.telran.gardenStore.exception;

public class CategoryWithNameAlreadyExistsException extends EntityAlreadyExistsException {
    public CategoryWithNameAlreadyExistsException(String message) {
        super(message);
    }
}
