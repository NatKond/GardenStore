package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableScheduling
@EnableAsync
class ScheduledServiceTest extends AbstractTest {

    @MockitoBean
    private OrderService orderService;

    @Test
    @DisplayName("Process orders with status created")
    void processCreatedOrders() {
        Order createdOrder = order2;

        when(orderService.getByStatusAndTimeAfter(eq(OrderStatus.CREATED), any(LocalDateTime.class)))
                .thenReturn(List.of(createdOrder));

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.CREATED),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(createdOrder);
        });

        assertEquals(OrderStatus.AWAITING_PAYMENT, createdOrder.getStatus());
    }

    @Test
    @DisplayName("Process orders with status awaiting payment")
    void processAwaitingPaymentOrders() {
        Order awaitingPaymentOrder = order1;

        when(orderService.getByStatusAndTimeAfter(eq(OrderStatus.AWAITING_PAYMENT), any(LocalDateTime.class)))
                .thenReturn(List.of(awaitingPaymentOrder));

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.AWAITING_PAYMENT),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(awaitingPaymentOrder);
        });

        assertEquals(OrderStatus.CANCELLED, awaitingPaymentOrder.getStatus());
    }

    @Test
    @DisplayName("Process orders with status paid")
    void processPaidOrders() {
        Order paidOrder = order3.toBuilder().status(OrderStatus.PAID).build();

        when(orderService.getByStatusAndTimeAfter(eq(OrderStatus.PAID), any(LocalDateTime.class)))
                .thenReturn(List.of(paidOrder));

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.PAID),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(paidOrder);
        });

        assertEquals(OrderStatus.SHIPPED, paidOrder.getStatus());
    }

    @Test
    @DisplayName("Process orders with status shipped")
    void processShippedOrders() {
        Order shippedOrder = order3.toBuilder().status(OrderStatus.SHIPPED).build();

        when(orderService.getByStatusAndTimeAfter(eq(OrderStatus.SHIPPED), any(LocalDateTime.class)))
                .thenReturn(List.of(shippedOrder));

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.SHIPPED),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(shippedOrder);
        });

        assertEquals(OrderStatus.DELIVERED, shippedOrder.getStatus());
    }

    @Test
    @DisplayName("Process orders : negative case")
    void processOrdersNegativeCase() {
        when(orderService.getByStatusAndTimeAfter(any(OrderStatus.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        verify(orderService, never()).update(any(Order.class));
    }

    @Test
    @DisplayName("Process orders : positive case")
    void processOrdersPositiveCase() {
        Order createdOrder1 = order1.toBuilder().status(OrderStatus.CREATED).build();
        Order createdOrder2 = order2;

        when(orderService.getByStatusAndTimeAfter(eq(OrderStatus.CREATED), any(LocalDateTime.class)))
                .thenReturn(List.of(createdOrder1, createdOrder2))
                .thenReturn(Collections.emptyList());

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.CREATED),
                    any(LocalDateTime.class));
            verify(orderService, times(2)).update(any(Order.class));
        });

        assertEquals(OrderStatus.AWAITING_PAYMENT, createdOrder1.getStatus());
        assertEquals(OrderStatus.AWAITING_PAYMENT, createdOrder2.getStatus());
    }
}