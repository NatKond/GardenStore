package de.telran.gardenStore.exception;

public class NoDiscountedProductsException extends RuntimeException {

    public NoDiscountedProductsException(String message) {
        super(message);
    }
}