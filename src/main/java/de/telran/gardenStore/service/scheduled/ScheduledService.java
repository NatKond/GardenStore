package de.telran.gardenStore.service.scheduled;

import de.telran.gardenStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final OrderService orderService;

    @Scheduled(cron = "0 */15 * * * *")
    public void changeOrderService() {
        orderService.getAllActive().stream()
                .filter(order -> order.getUpdatedAt().isAfter(LocalDateTime.now().minusMinutes(15)))
                .forEach(order -> orderService.updateStatus(order.getOrderId(),order.getStatus().next()));
    }
}
