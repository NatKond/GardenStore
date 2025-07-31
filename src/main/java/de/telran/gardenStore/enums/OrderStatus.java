package de.telran.gardenStore.enums;

public enum OrderStatus {
    CREATED,
    AWAITING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public OrderStatus next() {
        return switch (this){
            case CREATED -> AWAITING_PAYMENT;
            case AWAITING_PAYMENT, CANCELLED -> CANCELLED;
            case PAID -> SHIPPED;
            case SHIPPED, DELIVERED -> DELIVERED;
        };
    }
}