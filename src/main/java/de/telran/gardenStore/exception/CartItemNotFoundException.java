package de.telran.gardenStore.exception;

import jakarta.persistence.EntityNotFoundException;

public class CartItemNotFoundException extends EntityNotFoundException {

    public CartItemNotFoundException(String message) {
        super(message);
    }
}

