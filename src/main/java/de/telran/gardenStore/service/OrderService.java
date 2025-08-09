package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Order getById(Long orderId);

    List<Order> getAllForCurrentUser();

    List<Order> getAll();

    List<Order> getAllDeliveredForCurrentUser();

    List<Order> getByStatusAndTimeAfter(OrderStatus status, LocalDateTime updatedAt);

    Order create(String deliveryAddress, DeliveryMethod deliveryMethod, String contactPhone, Map<Long, Integer> productIdPerQuantityMap);

    void update (Order order);

    Order updateStatus(Long orderId, OrderStatus status);

    Order addItem(Long orderId, Long productId, Integer quantity);

    Order updateItem(Long orderItemId, Integer quantity);

    Order removeItem(Long orderItemId);

    Order cancel(Long orderId);
}