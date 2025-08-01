package de.telran.gardenStore.exception;

public class EmptyOrderException extends RuntimeException {

    public EmptyOrderException(String message) {
        super(message);
    }
}
