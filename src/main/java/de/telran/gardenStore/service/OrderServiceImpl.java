package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;

    @Override
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
    }

    @Override
    public List<Order> getAllByUserId(Long userId) {
        userService.getUserById(userId);
        return orderRepository.findAllByUserUserId(userId);
    }

    @Override
    @Transactional
    public Order create(Order order) {

        order.setStatus(OrderStatus.CREATED);
        for (OrderItem item : order.getItems()) {
            Cart cart = cartService.getCartByUser(order.getUser());
            cartService.processOrderItem(cart, item.getProduct().getProductId(), item.getQuantity());
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancel(Long orderId) {
        Order order = getById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}