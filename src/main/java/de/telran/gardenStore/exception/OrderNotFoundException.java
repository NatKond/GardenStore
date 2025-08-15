package de.telran.gardenStore.exception;

public class OrderNotFoundException extends EntityNotFoundException {

  public OrderNotFoundException(String message) {
    super(message);
  }
}