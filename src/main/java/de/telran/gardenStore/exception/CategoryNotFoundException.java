package de.telran.gardenStore.exception;

public class CategoryNotFoundException extends EntityNotFoundException {

  public CategoryNotFoundException(String message) {
    super(message);
  }
}
