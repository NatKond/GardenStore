package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    Order getById(Long orderId);

    List<Order> getAllByUserId(Long userId);

    List<Order> getAllActiveOrders();

    BigDecimal getTotalAmount(Long orderId);

    List<Order> getAllOrders(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate);

    Order create(Order order);

    Order updateStatus(Long orderId, OrderStatus status);

    Order update(Long orderId, Order order);

    Order addOrderItem(Long orderId, Long productId, Integer quantity);

    Order updateOrderItem(Long orderItemId, Integer quantity);

    Order removeOrderItem(Long orderItemId);

    Order cancel(Long orderId);
}