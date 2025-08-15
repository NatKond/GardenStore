package de.telran.gardenStore.exception;

public class OrderItemNotFoundException extends EntityNotFoundException {

    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
