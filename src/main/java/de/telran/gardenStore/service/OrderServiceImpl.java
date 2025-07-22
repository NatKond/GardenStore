package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.EmptyOrderException;
import de.telran.gardenStore.exception.OrderModificationException;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemService orderItemService;

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
    public List<Order> getAllActiveOrders() {
        return orderRepository.findAllByStatusNotIn(List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED));
    }

    @Override
    public BigDecimal getTotalAmount(Long orderId) {
        List<OrderItem> orderItems = getById(orderId).getItems();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            totalAmount = totalAmount.add(orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        return totalAmount;
    }

    @Override
    @Transactional
    public Order create(Order order) {
        Cart cart = cartService.getByUser(order.getUser());
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
                orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());

                processCartItem(cartItem, cartItems, orderItem.getQuantity());

            } else {
                iterator.remove();
            }
        }
        if (orderItems.isEmpty()) {
            throw new EmptyOrderException("Order is empty.");
        }
        cartService.update(cart);
        return orderRepository.save(order);
    }

    @Override
    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = getById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order addOrderItem(Long orderId, Long productId, Integer quantity) {
        Order order = getById(orderId);
        checkOrderCanBeModified(order);
        List<OrderItem> orderItems = order.getItems();
        Optional<OrderItem> orderItemExisting = orderItems.stream().filter(orderItem -> orderItem.getProduct().getProductId().equals(productId)).findFirst();
        if (orderItemExisting.isPresent()) {
            return updateOrderItem(orderItemExisting.get().getOrderItemId(), quantity);
        }

        Cart cart = cartService.getByUser(order.getUser());
        List<CartItem> cartItems = cart.getItems();
        Optional<CartItem> cartItemOptional = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getProductId().equals(productId))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            order.getItems().add(
                    OrderItem.builder()
                            .order(order)
                            .product(cartItem.getProduct())
                            .priceAtPurchase(cartItem.getProduct().getPrice())
                            .quantity(quantity)
                            .build());

            processCartItem(cartItem, cartItems, quantity);
        }

        cartService.update(cart);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order updateOrderItem(Long orderItemId, Integer quantity) {

        OrderItem orderItem = orderItemService.getById(orderItemId);
        Order order = orderItem.getOrder();
        checkOrderCanBeModified(order);

        Cart cart = cartService.getByUser(order.getUser());
        List<CartItem> cartItems = cart.getItems();
        Optional<CartItem> cartItemOptional = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getProductId().equals(orderItem.getProduct().getProductId()))
                .findFirst();

        cartItemOptional.ifPresent(cartItem -> processCartItem(cartItem, cartItems, quantity));

        orderItem.setQuantity(quantity);
        cartService.update(cart);
        return orderRepository.save(order);
    }

    @Override
    public Order removeOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemService.getById(orderItemId);

        Order order = orderItem.getOrder();
        order.getItems().remove(orderItem);

        if (order.getItems().isEmpty()) {
            throw new EmptyOrderException("Order is empty.");
        }

        return orderRepository.save(order);
    }

    @Override
    public Order cancel(Long orderId) {
        Order order = getById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    private void processCartItem(CartItem cartItem, List<CartItem> cartItems, Integer quantity) {
        if (cartItem.getQuantity() <= quantity) {
            cartItems.remove(cartItem);
            //cartService.deleteCartItem(cartItem.getCartItemId());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            //cartService.updateCartItem(cartItem.getCartItemId(), cartItem.getQuantity() - orderItem.getQuantity());
        }
    }

    private void checkOrderCanBeModified(Order order){
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderModificationException("Order cannot be modified in current status " + order.getStatus());
        }
    }
}