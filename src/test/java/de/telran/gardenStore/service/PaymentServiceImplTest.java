package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.IncorrectPaymentAmountException;
import de.telran.gardenStore.exception.OrderPaymentRejectedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest extends AbstractTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Process Payment : positive case")
    void processPaymentPositiveCase() {
        BigDecimal paymentAmount = orderResponseDto1.getTotalAmount();
        Order order = order1;
        Order expected = order1.toBuilder().status(OrderStatus.PAID).build();

        when(orderService.getTotalAmount(order.getOrderId())).thenReturn(paymentAmount);
        when(orderService.getById(order.getOrderId())).thenReturn(order);
        when(orderService.updateStatus(order.getOrderId(), OrderStatus.PAID))
                .thenReturn(expected);

        Order actual = paymentService.processPayment(
                order.getOrderId(),
                paymentAmount
        );

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Process Payment : negative case(incorrect amount)")
    void processPaymentNegativeCaseIncorrectAmount() {
        BigDecimal incorrectAmount = new BigDecimal("20.00");
        Order order = order1;

        when(orderService.getTotalAmount(order.getOrderId()))
                .thenReturn(orderResponseDto1.getTotalAmount());
        when(orderService.getById(order.getOrderId())).thenReturn(order);

        RuntimeException runtimeException = assertThrows(
                IncorrectPaymentAmountException.class,
                () -> paymentService.processPayment(order.getOrderId(), incorrectAmount)
        );
        assertEquals("Payment amount is incorrect", runtimeException.getMessage());
    }

    @Test
    @DisplayName("Process Payment : negative case(incorrect status)")
    void processPaymentNegativeCaseIncorrectStatus() {
        when(orderService.getTotalAmount(order2.getOrderId()))
                .thenReturn(product3.getDiscountPrice());
        when(orderService.getById(order2.getOrderId())).thenReturn(order2);

        RuntimeException runtimeException = assertThrows(
                OrderPaymentRejectedException.class,
                () -> paymentService.processPayment(order2.getOrderId(), product3.getDiscountPrice())
        );
        assertEquals("Order cannot be paid in current status: " + order2.getStatus(), runtimeException.getMessage());
    }
}