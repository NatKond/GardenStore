package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.OrderCreateRequestDto;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.dto.OrderShortResponseDto;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.IncorrectPaymentAmountException;
import de.telran.gardenStore.exception.OrderPaymentRejectedException;
import de.telran.gardenStore.service.OrderService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@Validated
public class PaymentControllerImpl implements PaymentController {

    private final OrderService orderService;

    private final Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @Override
    public OrderResponseDto processPayment(@Positive Long orderId, @Positive BigDecimal paymentAmount) {
        BigDecimal totalAmount = orderService.getTotalAmount(orderId);
        OrderStatus orderStatus = orderService.getById(orderId).getStatus();

        if (orderService.getById(orderId).getStatus() != OrderStatus.AWAITING_PAYMENT) {
            throw new OrderPaymentRejectedException("Order cannot be paid in current status: " + orderStatus);
        }

        if (totalAmount.equals(paymentAmount)) {
            return orderConverter.convertEntityToDto(orderService.updateStatus(orderId, OrderStatus.PAID));
        } else {
            throw new IncorrectPaymentAmountException("Payment amount is incorrect");
        }
    }
}
