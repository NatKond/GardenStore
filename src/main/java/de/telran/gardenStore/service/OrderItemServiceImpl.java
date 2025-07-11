package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotEditableException;
import de.telran.gardenStore.exception.OrderItemNotFoundException;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.OrderItemRepository;
import de.telran.gardenStore.repository.OrderRepository;
import de.telran.gardenStore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderItem addOrderItem(Long orderId, OrderItem orderItem) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        validateOrderEditable(order);

        var product = productRepository.findById(orderItem.getProduct().getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPriceAtOrder(product.getPrice());

        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public List<OrderItem> addAllOrderItems(List<OrderItem> orderItemList) {
        if (orderItemList == null || orderItemList.isEmpty()) {
            return Collections.emptyList();
        }

        var order = orderItemList.get(0).getOrder();
        if (order == null) {
            throw new IllegalArgumentException("Order items must have an associated order");
        }

        validateOrderEditable(order);

        return orderItemRepository.saveAll(orderItemList);
    }

    @Override
    @Transactional
    public OrderItem updateOrderItem(Long orderItemId, Integer quantity) {
        var orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));

        validateOrderEditable(orderItem.getOrder());

        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public void deleteOrderItem(Long orderItemId) {
        var orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));

        validateOrderEditable(orderItem.getOrder());

        orderItemRepository.delete(orderItem);
    }

    private void validateOrderEditable(Order order) {
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderNotEditableException(
                    "Cannot modify order in status: " + order.getStatus() +
                            ". Only orders with CREATED status can be modified.");
        }
    }
}