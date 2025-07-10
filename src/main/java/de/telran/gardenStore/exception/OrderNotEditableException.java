package de.telran.gardenStore.exception;
public class OrderNotEditableException extends RuntimeException {
    public OrderNotEditableException(String message) {
        super(message);
    }
}