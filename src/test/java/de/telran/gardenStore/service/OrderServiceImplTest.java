package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.EmptyOrderException;
import de.telran.gardenStore.exception.OrderCancellationException;
import de.telran.gardenStore.exception.OrderModificationException;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest extends AbstractTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Get order by ID : positive case")
    void getByIdPositiveCase() {
        Long orderId = order1.getOrderId();

        when(userService.getCurrent()).thenReturn(user1);
        when(orderRepository.findByUserAndOrderId(user1, orderId)).thenReturn(Optional.of(order1));

        Order actual = orderService.getById(orderId);

        assertNotNull(actual);
        assertEquals(order1, actual);
        verify(orderRepository).findByUserAndOrderId(user1, orderId);
    }

    @Test
    @DisplayName("Get order by ID : negative case")
    void getByIdNegativeCase() {
        Long orderId = 999L;

        when(userService.getCurrent()).thenReturn(user1);
        when(orderRepository.findByUserAndOrderId(user1, orderId)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.getById(orderId));
        assertEquals("Order with id " + orderId + " not found", exception.getMessage());
        verify(orderRepository).findByUserAndOrderId(user1, orderId);
    }

    @Test
    @DisplayName("Get all orders for current user")
    void getAll() {
        List<Order> expected = List.of(order1, order3);

        when(userService.getCurrent()).thenReturn(user1);
        when(orderRepository.findAllByUser(user1)).thenReturn(expected);

        List<Order> actual = orderService.getAll();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        verify(userService).getCurrent();
        verify(orderRepository).findAllByUser(user1);
    }

    @Test
    @DisplayName("Get all delivered orders for current user")
    void getAllDelivered() {
        List<Order> expected = List.of(order3);

        when(userService.getCurrent()).thenReturn(user1);
        when(orderRepository.findAllByUserAndStatus(user1, OrderStatus.DELIVERED)).thenReturn(expected);

        List<Order> actual = orderService.getAllDelivered();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        verify(userService).getCurrent();
        verify(orderRepository).findAllByUserAndStatus(user1, OrderStatus.DELIVERED);
    }

    @Test
    @DisplayName("Create order : positive case")
    void createPositiveCase() {
        Cart cart = cart1;
        Order orderCreated = orderToCreate.toBuilder()
                .orderId(3L)
                .build();

        when(userService.getCurrent()).thenReturn(orderCreated.getUser());
        when(cartService.getByUser(orderCreated.getUser())).thenReturn(cart);
        when(orderRepository.save(orderToCreate)).thenReturn(orderCreated);

        Order actual = orderService.create(
                orderToCreate.getDeliveryAddress(),
                orderToCreate.getDeliveryMethod(),
                orderToCreate.getContactPhone(),
                orderToCreate.getItems().stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getProductId(), OrderItem::getQuantity))
        );

        assertNotNull(actual);
        assertEquals(orderCreated, actual);
        verify(cartService).getByUser(user1);
        verify(orderRepository).save(orderToCreate);
        verify(cartService).update(cart);
    }

    @Test
    @DisplayName("Create order : negative case")
    void createNegativeCase() {
        List<OrderItem> orderItems = new ArrayList<>(List.of(
                OrderItem.builder()
                        .product(product3)
                        .quantity(3)
                        .build()
        ));

        Cart cart = cart1;

        Order orderToCreate = Order.builder()
                .user(user1)
                .deliveryAddress("123 Garden Street")
                .contactPhone(user1.getPhoneNumber())
                .deliveryMethod(DeliveryMethod.COURIER)
                .status(OrderStatus.CREATED)
                .items(orderItems)
                .build();

        when(userService.getCurrent()).thenReturn(orderToCreate.getUser());
        when(cartService.getByUser(orderToCreate.getUser())).thenReturn(cart);

        EmptyOrderException exception = assertThrows(EmptyOrderException.class, () -> orderService.create(
                orderToCreate.getDeliveryAddress(),
                orderToCreate.getDeliveryMethod(),
                orderToCreate.getContactPhone(),
                orderToCreate.getItems().stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getProductId(), OrderItem::getQuantity))
        ));

        assertEquals("Order is empty", exception.getMessage());
        verify(cartService).getByUser(user1);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update order status")
    void updateStatus() {
        Order orderToUpdate = order1;
        Long orderId = orderToUpdate.getOrderId();

        Order expected = order1.toBuilder()
                .status(OrderStatus.PAID)
                .build();

        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(orderRepository.findByUserAndOrderId(user1, orderId)).thenReturn(Optional.of(orderToUpdate));
        when(orderRepository.save(orderToUpdate)).thenReturn(expected);

        Order actual = orderService.updateStatus(orderId, OrderStatus.PAID);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Add order item : positive case")
    void addItemPositiveCase() {
        OrderItem orderItemToAdd = OrderItem.builder()
                .product(cartItem3.getProduct())
                .priceAtPurchase(cartItem3.getProduct().getDiscountPrice())
                .quantity(cartItem3.getQuantity() - 1)
                .build();

        Cart cart = cart2;

        Order orderToUpdate = order2;
        Long orderId = orderToUpdate.getOrderId();

        List<OrderItem> orderItemsUpdated = new ArrayList<>(order2.getItems());
        orderItemsUpdated.add(orderItemToAdd);

        BigDecimal totalAmount = orderItemsUpdated.stream()
                .map(orderItem4 -> orderItem4.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem4.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order orderUpdated = orderToUpdate.toBuilder()
                .items(orderItemsUpdated)
                .totalAmount(totalAmount)
                .build();

        List<CartItem> cartItemsUpdated = new ArrayList<>(List.of(
                cartItem3.toBuilder()
                        .quantity(cartItem3.getQuantity() - 1)
                        .build()));
        Cart cartUpdated = cart2.toBuilder().items(cartItemsUpdated).build();

        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(orderRepository.findByUserAndOrderId(user2, orderId)).thenReturn(Optional.of(orderToUpdate));
        when(cartService.getByUser(orderToUpdate.getUser())).thenReturn(cart);
        when(cartService.update(cart)).thenReturn(cartUpdated);
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);

        Order actual = orderService.addItem(orderId, orderItemToAdd.getProduct().getProductId(), orderItemToAdd.getQuantity());

        assertNotNull(actual);
        assertEquals(orderUpdated, actual);
        verify(cartService).getByUser(user2);
        verify(orderRepository).save(orderUpdated);
        verify(cartService).update(cart);
    }

    @Test
    @DisplayName("Add order item : positive case(existing product)")
    void addItemPositiveCaseExistingProduct() {
        int quantity = 2;
        Order orderToUpdate = order2;
        Long orderId = orderToUpdate.getOrderId();

        OrderItem orderItemToUpdate = orderItem3.toBuilder()
                .quantity(orderItem3.getQuantity() + quantity)
                .build();
        Long orderItemId = orderItemToUpdate.getOrderItemId();
        List<OrderItem> orderItemsUpdated = new ArrayList<>(List.of(orderItemToUpdate));

        orderItemToUpdate.setOrder(orderToUpdate);

        BigDecimal totalAmount = orderItemsUpdated.stream()
                .map(orderItem -> orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order orderUpdated = orderToUpdate.toBuilder()
                .items(orderItemsUpdated)
                .totalAmount(totalAmount)
                .build();

        Cart cart = cart2;

        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(orderRepository.findByUserAndOrderId(orderToUpdate.getUser(), orderId)).thenReturn(Optional.of(orderToUpdate));
        when(orderItemService.getById(orderItemId)).thenReturn(orderItem3);
        when(cartService.getByUser(orderToUpdate.getUser())).thenReturn(cart);
        when(cartService.update(cart)).thenReturn(cart);
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);

        Order actual = orderService.addItem(orderId, orderItemToUpdate.getProduct().getProductId(), quantity);

        assertNotNull(actual);
        assertEquals(orderUpdated, actual);
        verify(cartService).getByUser(user2);
        verify(orderItemService).getById(orderItemId);
        verify(orderRepository).save(orderUpdated);
    }

    @Test
    @DisplayName("Add order item : negative case")
    void addItemNegativeCase() {
        Order orderToUpdate = order1.toBuilder()
                .status(OrderStatus.AWAITING_PAYMENT)
                .build();
        Long orderId = orderToUpdate.getOrderId();

        OrderItem orderItem = OrderItem.builder()
                .product(cartItem3.getProduct())
                .quantity(cartItem3.getQuantity())
                .quantity(cartItem3.getQuantity())
                .build();
        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(orderRepository.findByUserAndOrderId(orderToUpdate.getUser(), orderId)).thenReturn(Optional.of(orderToUpdate));

        OrderModificationException exception = assertThrows(OrderModificationException.class, () -> orderService.addItem(orderId, orderItem.getProduct().getProductId(), orderItem.getQuantity()));
        assertEquals("Order cannot be modified in current status " + orderToUpdate.getStatus(), exception.getMessage());
        verify(orderRepository).findByUserAndOrderId(orderToUpdate.getUser(), orderId);
    }

    @Test
    @DisplayName("Update order item")
    void updateItem() {
        Cart cart = cart1;
        CartItem cartItemToRemove = cartItem1;

        Order orderToUpdate = order1.toBuilder().status(OrderStatus.CREATED).build();

        int quantityUpdated = 4;

        OrderItem orderItemToUpdate = orderItem1.toBuilder().order(orderToUpdate).build();
        Long orderItemId = orderItemToUpdate.getOrderItemId();
        OrderItem orderItemUpdated = orderItem1.toBuilder()
                .quantity(quantityUpdated)
                .build();

        List<OrderItem> orderItemsUpdated = new ArrayList<>(orderToUpdate.getItems());
        orderItemsUpdated.remove(orderItemToUpdate);
        orderItemsUpdated.add(orderItemUpdated);

        Order orderUpdated = orderToUpdate.toBuilder()
                .items(orderItemsUpdated)
                .build();

        List<CartItem> cartItemsUpdated = new ArrayList<>(cart.getItems());
        cartItemsUpdated.remove(cartItemToRemove);

        Cart cartUpdated = cart1.toBuilder().items(cartItemsUpdated).build();

        when(cartService.getByUser(orderToUpdate.getUser())).thenReturn(cart);
        when(cartService.update(cart)).thenReturn(cartUpdated);
        when(orderItemService.getById(orderItemId)).thenReturn(orderItemToUpdate);
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);

        Order actual = orderService.updateItem(orderItemId, quantityUpdated);

        assertNotNull(actual);
        assertEquals(orderUpdated, actual);
        verify(cartService).getByUser(user1);
        verify(orderRepository).save(orderUpdated);
        verify(orderItemService).getById(orderItemId);
        verify(cartService).update(cart);
    }

    @Test
    @DisplayName("Remove order item : positive case")
    void deleteItemPositiveCase() {
        Order orderToUpdate = order1.toBuilder().status(OrderStatus.CREATED).build();

        OrderItem orderItemToRemove = orderItem1.toBuilder().order(orderToUpdate).build();
        Long orderItemId = orderItemToRemove.getOrderItemId();

        List<OrderItem> orderItemsUpdated = new ArrayList<>(orderToUpdate.getItems());
        orderItemsUpdated.remove(orderItemToRemove);
        BigDecimal totalAmount = orderItemsUpdated.stream()
                .map(orderItem -> orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order orderUpdated = orderToUpdate.toBuilder()
                .items(orderItemsUpdated)
                .totalAmount(totalAmount)
                .build();

        when(orderItemService.getById(orderItemId)).thenReturn(orderItemToRemove);
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);

        Order actual = orderService.deleteItem(orderItemId);

        assertNotNull(actual);
        assertEquals(orderUpdated, actual);
        verify(orderItemService).getById(orderItemId);
        verify(orderRepository).save(orderUpdated);
    }

    @Test
    @DisplayName("Remove order item : negative case")
    void deleteItemNegativeCase() {
        OrderItem orderItemToRemove = orderItem3;
        Long orderItemId = orderItemToRemove.getOrderItemId();

        when(orderItemService.getById(orderItemId)).thenReturn(orderItemToRemove);

        EmptyOrderException exception = assertThrows(EmptyOrderException.class,
                () -> orderService.deleteItem(orderItemId));
        assertEquals("Order is empty", exception.getMessage());
    }

    @Test
    @DisplayName("Cancel order : positive case")
    void cancelOrderPositiveCase() {
        Long orderId = order2.getOrderId();
        Order orderToCancel = order2;

        Order cancelledOrder = orderToCancel.toBuilder()
                .status(OrderStatus.CANCELLED)
                .build();

        when(userService.getCurrent()).thenReturn(user2);
        when(orderRepository.findByUserAndOrderId(user2, orderId)).thenReturn(Optional.of(orderToCancel));
        when(orderRepository.save(cancelledOrder)).thenReturn(cancelledOrder);

        orderService.cancel(orderId);

        verify(orderRepository).findByUserAndOrderId(user2, orderId);
        verify(orderRepository).save(cancelledOrder);
    }

    @Test
    @DisplayName("Cancel order : negative case(order not found)")
    void cancelOrderNegativeCaseOrderNotFound() {
        Long orderId = 999L;
        when(userService.getCurrent()).thenReturn(user1);
        when(orderRepository.findByUserAndOrderId(user1, orderId)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.cancel(orderId));
        assertEquals("Order with id " + orderId + " not found", exception.getMessage());
        verify(orderRepository).findByUserAndOrderId(user1, orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Cancel order : negative case(order has incorrect status)")
    void cancelOrderNegativeCaseIncorrectStatus() {
        Long orderId = order1.getOrderId();
        OrderStatus status = OrderStatus.PAID;
        Order orderToCancel = order1.toBuilder().status(status).build();
        when(userService.getCurrent()).thenReturn(user1);
        when(orderRepository.findByUserAndOrderId(user1, orderId)).thenReturn(Optional.of(orderToCancel));

        OrderCancellationException exception = assertThrows(OrderCancellationException.class,
                () -> orderService.cancel(orderId));
        assertEquals("Order cannot be cancelled in current status " + status, exception.getMessage());
        verify(orderRepository).findByUserAndOrderId(user1, orderId);
        verify(orderRepository, never()).save(any());
    }
}