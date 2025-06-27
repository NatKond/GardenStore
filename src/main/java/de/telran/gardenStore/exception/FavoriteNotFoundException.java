package de.telran.gardenStore.exception;

public class FavoriteNotFoundException extends RuntimeException{
    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
