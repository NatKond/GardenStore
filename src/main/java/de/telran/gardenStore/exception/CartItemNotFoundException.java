package de.telran.gardenStore.exception;

public class CartItemNotFoundException extends EntityNotFoundException {

    public CartItemNotFoundException(String message) {
        super(message);
    }
}

