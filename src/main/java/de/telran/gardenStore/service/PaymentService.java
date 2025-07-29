package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.exception.IncorrectPaymentAmountException;
import de.telran.gardenStore.exception.OrderPaymentRejectedException;
import java.math.BigDecimal;

public interface PaymentService {
    OrderResponseDto processPayment(Long orderId, BigDecimal paymentAmount)
            throws IncorrectPaymentAmountException, OrderPaymentRejectedException;
}