package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import java.util.List;

public interface OrderService {
    Order getById(Long orderId);
    List<Order> getAllByUserId(Long userId);
    Order create(Order order);
    void cancel(Long orderId);
}