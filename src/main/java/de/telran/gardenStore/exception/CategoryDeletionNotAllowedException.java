package de.telran.gardenStore.exception;

public class CategoryDeletionNotAllowedException extends EntityDeletionNotAllowedException  {

    public CategoryDeletionNotAllowedException(String message) {
        super(message);
    }
}
