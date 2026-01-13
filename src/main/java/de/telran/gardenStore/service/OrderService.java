package de.telran.gardenStore.service;

import de.telran.enums.DeliveryMethod;
import de.telran.enums.OrderStatus;
import de.telran.gardenStore.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Order getById(Long orderId);

    List<Order> getAll();

    List<Order> getAllDelivered();

    List<Order> getByStatusAndTimeAfter(OrderStatus status, LocalDateTime updatedAt);

    Order create(String deliveryAddress, DeliveryMethod deliveryMethod, String contactPhone, Map<Long, Integer> productIdPerQuantityMap);

    void update (Order order);

    Order updateStatus(Long orderId, OrderStatus status);

    Order addItem(Long orderId, Long productId, Integer quantity);

    Order updateItem(Long orderItemId, Integer quantity);

    Order deleteItem(Long orderItemId);

    Order cancel(Long orderId);
}