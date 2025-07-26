package de.telran.gardenStore.exception;

public class CartAccessDeniedException extends RuntimeException {
    public CartAccessDeniedException(String message) {
        super(message);
    }
}
