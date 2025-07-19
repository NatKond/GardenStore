package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Get order by ID : positive case")
    void getByIdPositiveCase() {

        Long orderId = order1.getOrderId();
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
    @DisplayName("Get all orders by user ID")
    void getAllByUserId_ReturnsUserOrders() {

        Long userId = user1.getUserId();
        List<Order> expected = List.of(order1);

        when(userService.getUserById(userId)).thenReturn(user1);
        when(orderRepository.findAllByUser(user1)).thenReturn(expected);

        List<Order> actual = orderService.getAllByUserId(userId);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(userService).getUserById(userId);
        verify(orderRepository).findAllByUser(user1);
    }

    @Test
    @DisplayName("Create order : positive case")
    void createOrder_ValidOrder_ReturnsCreatedOrder() {

        List<OrderItem> orderItems = new ArrayList<>(List.of(
                OrderItem.builder()
                        .product(cartItem1.getProduct())
                        .quantity(cartItem1.getQuantity())
                        .build(),
                OrderItem.builder()
                        .product(cartItem2.getProduct())
                        .quantity(cartItem2.getQuantity())
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

        Order orderCreated = orderToCreate.toBuilder()
                .orderId(1L)
                .build();

        when(cartService.getByUser(user1)).thenReturn(cart);
        when(orderRepository.save(orderToCreate)).thenReturn(orderCreated);

        Order actual = orderService.create(orderToCreate);

        assertNotNull(actual);
        assertEquals(orderCreated, actual);
        verify(cartService).getByUser(user1);
        verify(orderRepository).save(orderToCreate);
        verify(cartService).update(cart);
    }

    @Test
    @DisplayName("Cancel order : positive case")
    void cancelOrderPositiveCase() {

        Long orderId = order2.getOrderId();
        Order orderToCancel = order2;

        Order cancelledOrder = orderToCancel.toBuilder()
                .status(OrderStatus.CANCELLED)
                .build();

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