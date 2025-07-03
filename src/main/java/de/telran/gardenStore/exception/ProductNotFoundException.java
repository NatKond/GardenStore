package de.telran.gardenStore.exception;

import jakarta.persistence.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {
  public ProductNotFoundException(String message) {
    super(message);
  }
}
