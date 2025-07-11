package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import java.util.List;

public interface OrderItemService {
    OrderItem addOrderItem(Long orderId, OrderItem orderItem);
    List<OrderItem> addAllOrderItems(List<OrderItem> orderItemList);
    OrderItem updateOrderItem(Long orderItemId, Integer quantity);
    void deleteOrderItem(Long orderItemId);
}