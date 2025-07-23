package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.OrderResponseDto;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
@RequestMapping("/v1/payment")
public interface PaymentController {

    @PostMapping()
    OrderResponseDto processPayment(@RequestParam @Positive Long orderId, @RequestParam @Positive BigDecimal paymentAmount);

}
