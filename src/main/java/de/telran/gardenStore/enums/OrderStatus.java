package de.telran.gardenStore.enums;

public enum OrderStatus {
    CREATED,
    AWAITING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public OrderStatus getNext() {
        return switch (this){
            case CREATED -> AWAITING_PAYMENT;
            case AWAITING_PAYMENT -> PAID;
            case PAID -> SHIPPED;
            case SHIPPED -> DELIVERED;
            default -> this;
        };
    }
}