package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.EmptyOrderException;
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
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order1));

        Order actual = orderService.getById(orderId);

        assertNotNull(actual);
        assertEquals(order1, actual);
        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("Get order by ID : negative case")
    void getByIdNegativeCase() {
        Long orderId = 999L;
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.getById(orderId));
        assertEquals("Order with id " + orderId + " not found", exception.getMessage());
        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("Get all orders for current user")
    void getAllForCurrentUser() {
        List<Order> expected = List.of(order1);

        when(userService.getCurrent()).thenReturn(user1);
        when(orderRepository.findAllByUser(user1)).thenReturn(expected);

        List<Order> actual = orderService.getAllForCurrentUser();

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(userService).getCurrent();
        verify(orderRepository).findAllByUser(user1);
    }

    @Test
    @DisplayName("Get all active orders")
    void getAllActive() {
        List<Order> expected = List.of(order1, order2);

        when(orderRepository.findAllByStatusNotIn(List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))).thenReturn(expected);

        List<Order> actual = orderService.getAllActive();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
        verify(orderRepository).findAllByStatusNotIn(List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED));
    }

    @Test
    @DisplayName("Get total amount by orderId")
    void getTotalAmount() {

        BigDecimal expected = new BigDecimal("28.47");

        when(userService.getCurrent()).thenReturn(order1.getUser());
        when(orderRepository.findById(order1.getOrderId())).thenReturn(Optional.of(order1));

        BigDecimal actual = orderService.getTotalAmount(order1.getOrderId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Create order : positive case")
    void createOrderPositiveCase() {
        Cart cart = cart1;
        Order orderCreated = orderToCreate.toBuilder()
                .orderId(3L)
                .build();

        when(userService.getCurrent()).thenReturn(orderCreated.getUser());
        when(cartService.getByUser(orderCreated.getUser())).thenReturn(cart);
        when(orderRepository.save(orderToCreate)).thenReturn(orderCreated);

        Order actual = orderService.create(orderToCreate);

        assertNotNull(actual);
        assertEquals(orderCreated, actual);
        verify(cartService).getByUser(user1);
        verify(orderRepository).save(orderToCreate);
        verify(cartService).update(cart);
    }

    @Test
    @DisplayName("Create order : negative case")
    void createOrderNegativeCase() {
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

        EmptyOrderException exception = assertThrows(EmptyOrderException.class, () -> orderService.create(orderToCreate));

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
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderToUpdate));
        when(orderRepository.save(orderToUpdate)).thenReturn(expected);

        Order actual = orderService.updateStatus(orderId, OrderStatus.PAID);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Add order item")
    void addOrderItem() {

        OrderItem orderItem = OrderItem.builder()
                .product(cartItem3.getProduct())
                .quantity(cartItem3.getQuantity())
                .quantity(cartItem3.getQuantity())
                .build();

        Cart cart = cart2;

        Order orderToUpdate = order2;
        Long orderId = orderToUpdate.getOrderId();

        List<OrderItem> orderItemsUpdated = new ArrayList<>(List.of(orderItem));

        orderItemsUpdated.add(orderItem);

        Order orderUpdated = orderToUpdate.toBuilder()
                .items(orderItemsUpdated)
                .build();

        List<CartItem> cartItemsUpdated = new ArrayList<>(List.of(cartItem3));
        cartItemsUpdated.remove(cartItem3);

        Cart cartUpdated = cart2.toBuilder().items(cartItemsUpdated).build();

        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(cartService.getByUser(orderToUpdate.getUser())).thenReturn(cart);
        when(cartService.update(cart)).thenReturn(cartUpdated);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderToUpdate));
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);

        Order actual = orderService.addOrderItem(orderId,orderItem.getProduct().getProductId(),orderItem.getQuantity());

        assertNotNull(actual);
        assertEquals(orderUpdated, actual);
        verify(cartService).getByUser(user2);
        verify(orderRepository).save(orderUpdated);
        verify(cartService).update(cart);
    }

    @Test
    @DisplayName("Update order item")
    void updateOrderItem() {

        Cart cart = cart1;
        CartItem cartItemToRemove = cartItem1;

        Order orderToUpdate = order1.toBuilder()
                .status(OrderStatus.CREATED)
                .build();

        Integer quantityUpdated = 4;

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

        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(cartService.getByUser(orderToUpdate.getUser())).thenReturn(cart);
        when(cartService.update(cart)).thenReturn(cartUpdated);
        when(orderItemService.getById(orderItemId)).thenReturn(orderItemToUpdate);
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);

        Order actual = orderService.updateOrderItem(orderItemId,quantityUpdated);

        assertNotNull(actual);
        assertEquals(orderUpdated, actual);
        verify(cartService).getByUser(user1);
        verify(orderRepository).save(orderUpdated);
        verify(cartService).update(cart);
    }

    @Test
    @DisplayName("Remove order item : positive case")
    void removeOrderItemPositiveCase() {
        Order orderToUpdate = order1.toBuilder()
                .status(OrderStatus.CREATED)
                .build();

        OrderItem orderItemToRemove = orderItem1.toBuilder().order(orderToUpdate).build();
        Long orderItemId = orderItemToRemove.getOrderItemId();

        List<OrderItem> orderItemsUpdated = new ArrayList<>(orderToUpdate.getItems());
        orderItemsUpdated.remove(orderItemToRemove);

        Order orderUpdated = orderToUpdate.toBuilder()
                .items(orderItemsUpdated)
                .build();

        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(orderItemService.getById(orderItemId)).thenReturn(orderItemToRemove);
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);

        Order actual = orderService.removeOrderItem(orderItemId);

        assertNotNull(actual);
        assertEquals(orderUpdated, actual);
        verify(orderItemService).getById(orderItemId);
        verify(orderRepository).save(orderUpdated);
    }

    @Test
    @DisplayName("Remove order item : negative case")
    void removeOrderItemNegativeCase() {
        OrderItem orderItemToRemove = orderItem3;
        Order orderToUpdate = orderItemToRemove.getOrder();
        Long orderItemId = orderItemToRemove.getOrderItemId();

        when(userService.getCurrent()).thenReturn(orderToUpdate.getUser());
        when(orderItemService.getById(orderItemId)).thenReturn(orderItemToRemove);

        EmptyOrderException exception = assertThrows(EmptyOrderException.class,
                () -> orderService.removeOrderItem(orderItemId));
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
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderToCancel));
        when(orderRepository.save(cancelledOrder)).thenReturn(cancelledOrder);

        orderService.cancel(orderId);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(cancelledOrder);
    }

    @Test
    @DisplayName("Cancel order : negative case")
    void cancelOrderNegativeCase() {

        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.cancel(orderId));
        assertEquals("Order with id " + orderId + " not found", exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
    }
}