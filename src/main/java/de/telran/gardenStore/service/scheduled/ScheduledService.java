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
        log.debug("{}: Created Orders", Thread.currentThread().getName());
        changeOrderStatus(orderService.getByStatusAndTimeAfter(OrderStatus.CREATED, LocalDateTime.now().minusMinutes(orderUpdatedAfter)));
    }

    @Async
    @Scheduled(cron = "${scheduled.orders.cron}")
    public void processAwaitingPaymentOrders() {
        log.debug("{}: Awaiting Payment Orders", Thread.currentThread().getName());
        changeOrderStatus(orderService.getByStatusAndTimeAfter(OrderStatus.AWAITING_PAYMENT, LocalDateTime.now().minusMinutes(orderUpdatedAfter)));
    }

    @Async
    @Scheduled(cron = "${scheduled.orders.cron}")
    public void processPaidOrders() {
        log.debug("{}: Paid Orders", Thread.currentThread().getName());
        changeOrderStatus(orderService.getByStatusAndTimeAfter(OrderStatus.PAID, LocalDateTime.now().minusMinutes(orderUpdatedAfter)));
    }

    @Async
    @Scheduled(cron = "${scheduled.orders.cron}")
    public void processShippedOrders() {
        log.debug("{}: Shipped Orders", Thread.currentThread().getName());
        changeOrderStatus(orderService.getByStatusAndTimeAfter(OrderStatus.SHIPPED, LocalDateTime.now().minusMinutes(orderUpdatedAfter)));
    }

    private void changeOrderStatus(List<Order> orders) {
        orders.forEach(order -> {
            order.setStatus(order.getStatus().getNext());
            orderService.update(order);
        });
    }
}
