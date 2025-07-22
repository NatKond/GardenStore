package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    Order getById(Long orderId);

    List<Order> getAllForCurrentUser();

    List<Order> getAllActive();

    BigDecimal getTotalAmount(Long orderId);

    Order create(Order order);

    Order updateStatus(Long orderId, OrderStatus status);

    Order addOrderItem(Long orderId, Long productId, Integer quantity);

    Order updateOrderItem(Long orderItemId, Integer quantity);

    Order removeOrderItem(Long orderItemId);

    void cancel(Long orderId);
}