package de.telran.gardenStore.exception;

public class UserDeletionNotAllowedException extends EntityDeletionNotAllowedException {

    public UserDeletionNotAllowedException(String message) {
        super(message);
    }
}
