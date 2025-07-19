package de.telran.gardenStore.controller;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface PaymentController {
    ResponseEntity<?> processPayment(Long orderId, BigDecimal amount);

}
