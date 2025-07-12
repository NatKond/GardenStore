package de.telran.gardenStore.exception;

import jakarta.persistence.EntityNotFoundException;

public class CartNotFoundException extends EntityNotFoundException {
    public CartNotFoundException(String message) {
        super(message);
    }
}