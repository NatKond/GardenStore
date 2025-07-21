package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Order getById(Long orderId);

    List<Order> getAllByUserId(Long userId);

    Order create(Order order);

    Order cancel(Long orderId);

    List<Order> getAllOrders(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate);

    Order update(Long orderId, Order order);
}