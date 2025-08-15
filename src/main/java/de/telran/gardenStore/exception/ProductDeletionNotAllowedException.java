package de.telran.gardenStore.exception;

public class ProductDeletionNotAllowedException extends EntityDeletionNotAllowedException {

    public ProductDeletionNotAllowedException(String message) {
        super(message);
    }
}
