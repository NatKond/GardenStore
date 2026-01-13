package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.IncorrectPaymentAmountException;
import de.telran.gardenStore.exception.OrderPaymentRejectedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderService orderService;

    @Override
    public Order processPayment(Long orderId, BigDecimal paymentAmount) {
        Order order = orderService.getById(orderId);
        OrderStatus status = order.getStatus();

        if (status != OrderStatus.AWAITING_PAYMENT) {
            throw new OrderPaymentRejectedException(
                    "Order cannot be paid in current status: " + status);
        }

        if (!order.getTotalAmount().equals(paymentAmount)) {
            throw new IncorrectPaymentAmountException("Payment amount is incorrect");
        }

        log.debug("Attempt to save paid order by user {}:\norderId = {} with {}\ntotal amount = {}",
                order.getUser().getEmail(),
                order.getOrderId(),
                order.getItems().stream().map(item -> "\n- " + item).collect(Collectors.joining("")),
                order.getTotalAmount());
        return orderService.updateStatus(orderId, OrderStatus.PAID);
    }
}