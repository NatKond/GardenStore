package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.OrderRequestCreateDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final CartService cartService; // Необходимо создать

    @Override
    @Transactional
    public Order createOrder(OrderRequestCreateDto orderDto) {
        AppUser user = userService.getUserById(orderDto.getUserId());

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.CREATED)
                .build();

        order = orderRepository.save(order);

        List<OrderItem> items = orderDto.getItems().stream()
                .map(itemDto -> {
                    OrderItem item = convertToOrderItem(itemDto, order);
                    adjustCartItem(user, item); // Логика работы с корзиной
                    return item;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItemService.addAllOrderItems(items));
        return order;
    }
    @Override
    public void delete(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
    private void adjustCartItem(AppUser user, OrderItem item) {
        // Логика изменения/удаления элементов корзины
    }
}