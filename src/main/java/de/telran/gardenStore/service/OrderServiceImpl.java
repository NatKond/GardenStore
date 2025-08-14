package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.EmptyOrderException;
import de.telran.gardenStore.exception.OrderCancellationException;
import de.telran.gardenStore.exception.OrderModificationException;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemService orderItemService;

    private final CartService cartService;

    private final UserService userService;

    @Override
    public Order getById(Long orderId) {
        return orderRepository.findByUserAndOrderId(userService.getCurrent(), orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAllByUser(userService.getCurrent());
    }

    @Override
    public List<Order> getAllDelivered(){
        return orderRepository.findAllByUserAndStatus(userService.getCurrent(), OrderStatus.DELIVERED);
    }

    @Override
    public List<Order> getByStatusAndTimeAfter(OrderStatus status, LocalDateTime updatedAt) {
        return orderRepository.findByStatusAndUpdatedAtAfter(status, updatedAt);
    }

    @Override
    @Transactional
    public Order create(String deliveryAddress, DeliveryMethod deliveryMethod, String contactPhone, Map<Long, Integer> productIdPerQuantityMap) {
        AppUser user = userService.getCurrent();

        Order order = Order.builder()
                .user(user)
                .deliveryAddress(deliveryAddress)
                .contactPhone(contactPhone != null ? contactPhone : user.getPhoneNumber())
                .deliveryMethod(deliveryMethod)
                .build();

        Cart cart = cartService.getByUser(user);
        List<CartItem> cartItems = cart.getItems();

        Map<Long, CartItem> productIdPerCartItemMap = cartItems.stream()
                .collect(Collectors.toMap(cartItem -> cartItem.getProduct().getProductId(), cartItem -> cartItem));

        productIdPerQuantityMap.forEach((productId, quantity) -> {
            if (productIdPerCartItemMap.containsKey(productId)) {
                CartItem cartItem = productIdPerCartItemMap.get(productId);
                order.getItems().add(createOrderItem(quantity, cartItem, order));
                editCartItemList(cartItem, cartItems, quantity);
            }
        });
        checkOrderNotEmpty(order);
        order.setTotalAmount(getTotalAmount(order));

        cartService.update(cart);

        return orderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = getById(orderId);
        order.setStatus(status);

        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order addItem(Long orderId, Long productId, Integer quantity) {
        Order order = getById(orderId);
        checkOrderCanBeModified(order);

        Optional<OrderItem> orderItemExistingOptional = findOrderItemByProductId(order.getItems(), productId);
        if (orderItemExistingOptional.isPresent()) {
            OrderItem orderItemExisting = orderItemExistingOptional.get();
            return updateItem(orderItemExisting.getOrderItemId(), orderItemExisting.getQuantity() + quantity);
        }

        Cart cart = cartService.getByUser(order.getUser());
        List<CartItem> cartItems = cart.getItems();
        Optional<CartItem> cartItem = findCartItemByProductId(cartItems, productId);

        if (cartItem.isPresent()) {
            CartItem cartItemExisting = cartItem.get();
            order.getItems().add(createOrderItem(quantity, cartItemExisting, order));
            editCartItemList(cartItemExisting, cartItems, quantity);
        }

        order.setTotalAmount(getTotalAmount(order));
        cartService.update(cart);

        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order updateItem(Long orderItemId, Integer quantity) {
        OrderItem orderItem = orderItemService.getById(orderItemId);
        Order order = orderItem.getOrder();
        checkOrderCanBeModified(order);

        Cart cart = cartService.getByUser(order.getUser());
        List<CartItem> cartItems = cart.getItems();
        Long productId = orderItem.getProduct().getProductId();
        Optional<CartItem> cartItem = findCartItemByProductId(cartItems, productId);

        cartItem.ifPresent(item -> editCartItemList(item, cartItems, quantity - orderItem.getQuantity()));

        orderItem.setQuantity(quantity);
        order.setTotalAmount(getTotalAmount(order));

        cartService.update(cart);

        return orderRepository.save(order);
    }

    @Override
    public Order removeItem(Long orderItemId) {
        OrderItem orderItem = orderItemService.getById(orderItemId);

        Order order = orderItem.getOrder();
        checkOrderCanBeModified(order);
        order.getItems().remove(orderItem);

        checkOrderNotEmpty(order);
        order.setTotalAmount(getTotalAmount(order));

        return orderRepository.save(order);
    }

    @Override
    public Order cancel(Long orderId) {
        Order order = getById(orderId);
        OrderStatus status = order.getStatus();
        if (status != OrderStatus.CREATED && status != OrderStatus.AWAITING_PAYMENT) {
            throw new OrderCancellationException("Order cannot be cancelled in current status " + status);
        }
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    private BigDecimal getTotalAmount(Order order) {
        return order.getItems().stream().map(orderItem -> orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void editCartItemList(CartItem cartItem, List<CartItem> cartItems, Integer quantity) {
        if (cartItem.getQuantity() <= quantity) {
            cartItems.remove(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
        }
    }

    private Optional<CartItem> findCartItemByProductId(List<CartItem> cartItems, Long productId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getProductId().equals(productId))
                .findFirst();
    }

    private Optional<OrderItem> findOrderItemByProductId(List<OrderItem> orderItems, Long productId) {
        return orderItems.stream()
                .filter(orderItem -> orderItem.getProduct().getProductId().equals(productId))
                .findFirst();
    }

    private OrderItem createOrderItem(Integer quantity, CartItem cartItem, Order order) {
        Product product = cartItem.getProduct();
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .priceAtPurchase(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice())
                .build();
    }

    private void checkOrderNotEmpty(Order order) {
        if (order.getItems().isEmpty()) {
            throw new EmptyOrderException("Order is empty");
        }
    }

    private void checkOrderCanBeModified(Order order) {
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderModificationException("Order cannot be modified in current status " + order.getStatus());
        }
    }
}