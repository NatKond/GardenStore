package de.telran.gardenStore.exception;

public class OrderPaymentRejectedException extends RuntimeException {

    public OrderPaymentRejectedException(String message) {
        super(message);
    }
}
