package de.telran.gardenStore.exception;

import jakarta.persistence.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {

  public OrderNotFoundException(String message) {
    super(message);
  }
}