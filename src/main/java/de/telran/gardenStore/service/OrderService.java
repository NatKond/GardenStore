package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.OrderRequestCreateDto;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    public Order createOrder(OrderRequestCreateDto orderDto) {
        Order order = Order.builder()
                .status(OrderStatus.CREATED)
                .build();

        order = orderRepository.save(order);

        List<OrderItem> items = orderItemService.addAllOrderItems(
                orderDto.getItems().stream()
                        .map(itemDto -> OrderItem.builder()
                                .order(order)
                                .product(Product.builder().productId(itemDto.getProductId()).build())
                                .quantity(itemDto.getQuantity())
                                .build())
                        .toList()
        );

        order.setOrderItems(items);
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}