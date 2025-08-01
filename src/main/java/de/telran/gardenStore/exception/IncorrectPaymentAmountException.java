package de.telran.gardenStore.exception;

public class IncorrectPaymentAmountException extends RuntimeException {

    public IncorrectPaymentAmountException(String message) {
        super(message);
    }
}
