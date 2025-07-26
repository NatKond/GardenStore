package de.telran.gardenStore.exception;

public class FavoriteAccessDeniedException extends RuntimeException {
    public FavoriteAccessDeniedException(String message) {
        super(message);
    }
}
