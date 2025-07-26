package de.telran.gardenStore.service.scheduled;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final OrderService orderService;

    @Scheduled(cron = "0 */5 * * * *")
    public void changeOrderService() {
        List<Order> orders = orderService.getAllActive().stream()
                .filter(order -> order.getUpdatedAt().isAfter(LocalDateTime.now().minusMinutes(15)))
                .peek(order -> order.setStatus(order.getStatus().next()))
                .toList();

        orderService.updateAll(orders);
    }
}
