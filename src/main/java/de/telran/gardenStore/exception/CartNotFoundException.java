package de.telran.gardenStore.exception;

public class CartNotFoundException extends EntityNotFoundException {

    public CartNotFoundException(String message) {
        super(message);
    }
}