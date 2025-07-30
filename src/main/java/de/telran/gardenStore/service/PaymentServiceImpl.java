package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.IncorrectPaymentAmountException;
import de.telran.gardenStore.exception.OrderPaymentRejectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderService orderService;

    @Override
    public Order processPayment(Long orderId, BigDecimal paymentAmount) {
        BigDecimal totalAmount = orderService.getTotalAmount(orderId);
        Order order = orderService.getById(orderId);
        OrderStatus status = order.getStatus();

        if (status != OrderStatus.AWAITING_PAYMENT) {
            throw new OrderPaymentRejectedException(
                    "Order cannot be paid in current status: " + status
            );
        }

        if (!totalAmount.equals(paymentAmount)) {
            throw new IncorrectPaymentAmountException("Payment amount is incorrect");
        }

        return orderService.updateStatus(orderId, OrderStatus.PAID);
    }
}