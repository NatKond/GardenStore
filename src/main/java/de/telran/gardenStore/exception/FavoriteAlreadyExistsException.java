package de.telran.gardenStore.exception;

public class FavoriteAlreadyExistsException extends EntityAlreadyExistsException {

    public FavoriteAlreadyExistsException(String message) {
        super(message);
    }
}
