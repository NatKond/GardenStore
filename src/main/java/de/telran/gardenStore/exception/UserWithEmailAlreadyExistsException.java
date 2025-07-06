package de.telran.gardenStore.exception;

public class UserWithEmailAlreadyExistsException extends EntityAlreadyExistsException {
    public UserWithEmailAlreadyExistsException(String message) {
        super(message);
    }
}
