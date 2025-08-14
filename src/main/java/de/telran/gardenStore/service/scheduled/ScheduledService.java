package de.telran.gardenStore.service.scheduled;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledService {

    private final OrderService orderService;

    @Value("${scheduled.orders.updated.after.minutes}")
    private long orderUpdatedAfter;

    @Async
    @Scheduled(cron = "${scheduled.orders.cron}")
    public void processCreatedOrders() {
        List<Order> ordersCreate = orderService.getByStatusAndTimeAfter(OrderStatus.CREATED, LocalDateTime.now().minusMinutes(orderUpdatedAfter));
        logOrdersToUpdate(ordersCreate, OrderStatus.CREATED);
        changeOrderStatus(ordersCreate);
    }

    @Async
    @Scheduled(cron = "${scheduled.orders.cron}")
    public void processAwaitingPaymentOrders() {
        List<Order> ordersAwaitingPayment = orderService.getByStatusAndTimeAfter(OrderStatus.AWAITING_PAYMENT, LocalDateTime.now().minusMinutes(orderUpdatedAfter));
        logOrdersToUpdate(ordersAwaitingPayment, OrderStatus.AWAITING_PAYMENT);
        changeOrderStatus(ordersAwaitingPayment);
    }

    @Async
    @Scheduled(cron = "${scheduled.orders.cron}")
    public void processPaidOrders() {
        List<Order> ordersPaid = orderService.getByStatusAndTimeAfter(OrderStatus.PAID, LocalDateTime.now().minusMinutes(orderUpdatedAfter));
        logOrdersToUpdate(ordersPaid, OrderStatus.PAID);
        changeOrderStatus(ordersPaid);
    }

    @Async
    @Scheduled(cron = "${scheduled.orders.cron}")
    public void processShippedOrders() {
        List<Order> ordersShipped = orderService.getByStatusAndTimeAfter(OrderStatus.SHIPPED, LocalDateTime.now().minusMinutes(orderUpdatedAfter));
        logOrdersToUpdate(ordersShipped, OrderStatus.SHIPPED);
        changeOrderStatus(ordersShipped);
    }

    private void logOrdersToUpdate(List<Order> orders, OrderStatus status) {
        if (!orders.isEmpty()) {
            log.debug("Scheduled service updating {} Orders with status = {}, Ids : {}",
                    orders.size(),
                    status,
                    orders.stream().map(order -> String.valueOf(order.getOrderId())).collect(Collectors.joining(", ")));
        }
    }

    private void changeOrderStatus(List<Order> orders) {
        orders.forEach(order -> {
            order.setStatus(order.getStatus().getNext());
            orderService.update(order);
        });
    }
}
