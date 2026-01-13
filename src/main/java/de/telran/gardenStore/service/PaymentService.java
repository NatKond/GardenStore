package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;

import java.math.BigDecimal;

public interface PaymentService {

    Order processPayment(Long orderId, BigDecimal paymentAmount);
}