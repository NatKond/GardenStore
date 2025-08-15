package de.telran.gardenStore.exception;

public class ProductNotFoundException extends EntityNotFoundException {

  public ProductNotFoundException(String message) {
    super(message);
  }
}
