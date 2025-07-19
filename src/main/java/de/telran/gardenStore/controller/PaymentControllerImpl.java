package de.telran.gardenStore.controller;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/payments")
public class PaymentControllerImpl implements PaymentController {
    private final OrderService orderService;

    @Override
    @PostMapping
    public ResponseEntity<?> processPayment(Long orderId, BigDecimal amount) {
        Order order = orderService.getById(orderId);
        List<OrderItem> orderItemList = order.getItems();
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemList) {
            sum = sum.add(orderItem.getPriceAtPurchase());
        }
        if (sum.compareTo(amount) == 0) {
            order.setStatus(OrderStatus.PAID);
        } else {
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok().build();
    }
}
