package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.OrderResponseDto;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public interface PaymentController {

    OrderResponseDto processPayment(@Positive Long orderId, @Positive BigDecimal paymentAmount);

}
