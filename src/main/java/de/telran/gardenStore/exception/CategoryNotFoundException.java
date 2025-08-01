package de.telran.gardenStore.exception;

import jakarta.persistence.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {

  public CategoryNotFoundException(String message) {
    super(message);
  }
}
