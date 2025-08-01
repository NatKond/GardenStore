package de.telran.gardenStore.exception;

import jakarta.persistence.EntityNotFoundException;

public class FavoriteNotFoundException extends EntityNotFoundException {

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
