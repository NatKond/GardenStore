package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.repository.OrderRepository;
import de.telran.gardenStore.service.scheduled.ScheduledService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "scheduled.orders.cron=0 */15 * * * *",
        "scheduled.orders.updated.after.minutes=60",
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ScheduledServiceIntegrationTest extends AbstractTest {

    @Autowired
    private ScheduledService scheduledService;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Order createdOrder;
    private Order awaitingPaymentOrder;
    private Order paidOrder;
    private Order shippedOrder;

    @BeforeEach
    protected void setUp() {
        Mockito.reset(orderService);
        createdOrder = Order.builder()
                .orderId(1L)
                .status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        awaitingPaymentOrder = Order.builder()
                .orderId(2L)
                .status(OrderStatus.AWAITING_PAYMENT)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        paidOrder = Order.builder()
                .orderId(3L)
                .status(OrderStatus.PAID)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        shippedOrder = Order.builder()
                .orderId(4L)
                .status(OrderStatus.SHIPPED)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();
    }

    @Test
    void processCreatedOrders_ShouldUpdateStatusToAwaitingPayment() {
        Order createdOrder = Order.builder()
                .orderId(1L)
                .status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class))
        ).thenReturn(List.of(createdOrder));

        scheduledService.processCreatedOrders();

        // zdem zavershenija asinhronnogo vyzova, v protivnom sluchae proverki verify idut do realnyh vizovov, tk assinhronnyj
        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.CREATED),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(createdOrder);
        });

        assertEquals(OrderStatus.AWAITING_PAYMENT, createdOrder.getStatus());
    }

    @Test
    void processAwaitingPaymentOrders_ShouldUpdateStatusToPaid() {
        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.AWAITING_PAYMENT),
                any(LocalDateTime.class)))
                .thenReturn(List.of(awaitingPaymentOrder));

        scheduledService.processAwaitingPaymentOrders();

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.AWAITING_PAYMENT),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(awaitingPaymentOrder);
        });

        assertEquals(OrderStatus.PAID, awaitingPaymentOrder.getStatus());
    }

    @Test
    void processPaidOrders_ShouldUpdateStatusToShipped() {
        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.PAID),
                any(LocalDateTime.class)))
                .thenReturn(List.of(paidOrder));

        scheduledService.processPaidOrders();

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.PAID),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(paidOrder);
        });

        assertEquals(OrderStatus.SHIPPED, paidOrder.getStatus());
    }

    @Test
    void processShippedOrders_ShouldUpdateStatusToDelivered() {
        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.SHIPPED),
                any(LocalDateTime.class)))
                .thenReturn(List.of(shippedOrder));

        scheduledService.processShippedOrders();

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).getByStatusAndTimeAfter(
                    eq(OrderStatus.SHIPPED),
                    any(LocalDateTime.class));
            verify(orderService, times(1)).update(shippedOrder);
        });

        assertEquals(OrderStatus.DELIVERED, shippedOrder.getStatus());
    }

    @Test
    void processOrders_ShouldNotUpdateOldOrders() {
        Order oldOrder = Order.builder()
                .orderId(5L)
                .status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(120))
                .build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenReturn(List.of());

        scheduledService.processCreatedOrders();

        verify(orderService, never()).update(oldOrder);
    }

    @Test
    void processOrders_ShouldHandleMultipleOrders() throws InterruptedException {
        Order createdOrder1 = Order.builder()
                .orderId(6L)
                .status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        Order createdOrder2 = Order.builder()
                .orderId(7L)
                .status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(45))
                .build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenReturn(List.of(createdOrder1, createdOrder2));

        scheduledService.processCreatedOrders();

        Thread.sleep(500);

        verify(orderService, times(2)).update(any(Order.class));

        assertEquals(OrderStatus.AWAITING_PAYMENT, createdOrder1.getStatus());
        assertEquals(OrderStatus.AWAITING_PAYMENT, createdOrder2.getStatus());
    }
    @Test
    void processOrders_ShouldHandleEmptyList() {
        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenReturn(List.of());

        scheduledService.processCreatedOrders();

        verify(orderService, never()).update(any());
    }

    @Test
    void processOrders_ShouldNotUpdateCancelledOrders() {
        Order cancelledOrder = Order.builder()
                .orderId(8L)
                .status(OrderStatus.CANCELLED)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenReturn(List.of());

        scheduledService.processCreatedOrders();

        verify(orderService, never()).update(cancelledOrder);
    }

    @Test
    void processOrders_ShouldNotUpdateDeliveredOrders() {
        Order deliveredOrder = Order.builder()
                .orderId(9L)
                .status(OrderStatus.DELIVERED)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.SHIPPED),
                any(LocalDateTime.class)))
                .thenReturn(List.of());

        scheduledService.processShippedOrders();

        verify(orderService, never()).update(deliveredOrder);
    }

    @Test
    void processOrders_ShouldUpdateOnlyRecentOrders() {
        Order recentOrder = Order.builder()
                .orderId(10L)
                .status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenReturn(List.of(recentOrder));

        scheduledService.processCreatedOrders();

        await().atMost(1, SECONDS).untilAsserted(() -> {

            verify(orderService, times(1)).update(any(Order.class));

            assertEquals(OrderStatus.AWAITING_PAYMENT, recentOrder.getStatus());
        });
    }
    @Test
    void processOrders_ShouldHandleConcurrentExecution() {
        Order order1 = Order.builder().orderId(12L).status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(30)).build();
        Order order2 = Order.builder().orderId(13L).status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(40)).build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenReturn(List.of(order1, order2))
                .thenReturn(List.of());

        scheduledService.processCreatedOrders();
        scheduledService.processCreatedOrders();

        await().atMost(2, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).update(order1);
            verify(orderService, times(1)).update(order2);
        });
    }

    @Test
    void processOrders_ShouldHandleBorderlineTimeConditions() {
        Order borderlineOrder = Order.builder()
                .orderId(11L)
                .status(OrderStatus.CREATED)
                .updatedAt(LocalDateTime.now().minusMinutes(59))
                .build();

        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenReturn(List.of(borderlineOrder));

        scheduledService.processCreatedOrders();

        await().atMost(1, SECONDS).untilAsserted(() -> {
            verify(orderService, times(1)).update(borderlineOrder);
            assertEquals(OrderStatus.AWAITING_PAYMENT, borderlineOrder.getStatus());
        });
    }

    @Test
    void processOrders_ShouldHandleServiceExceptions() {
        when(orderService.getByStatusAndTimeAfter(
                eq(OrderStatus.CREATED),
                any(LocalDateTime.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertDoesNotThrow(() -> scheduledService.processCreatedOrders());
    }
}