package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.OrderCreateRequestDto;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.dto.OrderShortResponseDto;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.service.PaymentService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@Validated
public class PaymentControllerImpl implements PaymentController {

    private final PaymentService paymentService;

    private final Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @Override
    public OrderResponseDto processPayment(@Positive Long orderId, @Positive BigDecimal paymentAmount) {
        return orderConverter.convertEntityToDto(paymentService.processPayment(orderId, paymentAmount));
    }
}