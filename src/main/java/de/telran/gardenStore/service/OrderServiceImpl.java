package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
        return orderRepository.findAllByUser(userService.getUserById(userId));
    }

    @Override
    @Transactional
    public Order create(Order order) {

        Cart cart = cartService.getCartByUser(order.getUser());
        List<CartItem> cartItems = cart.getItems();
        List<OrderItem> orderItems = order.getItems();
        Iterator<OrderItem> iterator = orderItems.iterator();
        while (iterator.hasNext()) {
            OrderItem orderItem = iterator.next();
            Optional<CartItem> cartItemOptional = cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().getProductId().equals(orderItem.getProduct().getProductId()))
                    .findFirst();

            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                if (cartItem.getQuantity() <= orderItem.getQuantity()) {
                    //cartService.deleteCartItem(cartItem.getCartItemId());
                    cartItems.remove(cartItem);
                } else {
                    //cartService.updateCartItem(cartItem.getCartItemId(), cartItem.getQuantity() - orderItem.getQuantity());
                    cartItem.setQuantity(cartItem.getQuantity() - orderItem.getQuantity());
                }
            } else {
                iterator.remove();
            }
        }
        cartService.update(cart);
        return orderRepository.save(order);
    }

    @Override
    public void cancel(Long orderId) {
        Order order = getById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}