package de.telran.gardenStore.exception;

public class EntityDeletionNotAllowedException extends RuntimeException {

    public EntityDeletionNotAllowedException(String message) {
        super(message);
    }
}
