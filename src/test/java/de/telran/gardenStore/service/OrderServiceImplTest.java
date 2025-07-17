package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.repository.OrderRepository;
import jakarta.transaction.Transactional;
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
    @DisplayName("Get order by ID - positive case")
    void getById_ExistingOrder_ReturnsOrder() {

        Long orderId = order1.getOrderId();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order1));


        Order result = orderService.getById(orderId);


        assertNotNull(result);
        assertEquals(order1, result);
        verify(orderRepository).findById(orderId);
    }

    @Test
    @DisplayName("Get order by ID - negative case (order not found)")
    void getById_NonExistingOrder_ThrowsException() {

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
        List<Order> expectedOrders = List.of(order1);
        when(userService.getUserById(userId)).thenReturn(user1);
        when(orderRepository.findAllByUser(user1)).thenReturn(expectedOrders);

        List<Order> result = orderService.getAllByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedOrders, result);
        verify(userService).getUserById(userId);
        verify(orderRepository).findAllByUser(user1);
    }

    @Test
    @DisplayName("Create order - positive case")
    @Transactional
    void createOrder_ValidOrder_ReturnsCreatedOrder() {
        List<CartItem> cartItems = new ArrayList<>(List.of(cartItem1, cartItem2));
        List<OrderItem> orderItems = new ArrayList<>(List.of(
                OrderItem.builder()
                        .product(product1)
                        .quantity(2)
                        .build(),
                OrderItem.builder()
                        .product(product2)
                        .quantity(1)
                        .build()
        ));

        Cart cart = cart1.toBuilder()
                .items(cartItems)
                .build();

        Order orderToCreate = Order.builder()
                .user(user1)
                .deliveryAddress("123 Garden Street")
                .contactPhone(user1.getPhoneNumber())
                .deliveryMethod(DeliveryMethod.COURIER)
                .status(OrderStatus.CREATED)
                .items(orderItems)
                .build();

        Order savedOrder = orderToCreate.toBuilder()
                .orderId(1L)
                .build();

        when(cartService.getByUser(user1)).thenReturn(cart);
        when(orderRepository.save(orderToCreate)).thenReturn(savedOrder);

        Order result = orderService.create(orderToCreate);

        assertNotNull(result);
        assertEquals(savedOrder, result);
        verify(cartService).getByUser(user1);
        verify(orderRepository).save(orderToCreate);
        verify(cartService).update(cart);
    }
    @Test
    @DisplayName("Cancel order - positive case")
    void cancelOrder_ExistingOrder_UpdatesStatus() {

        Long orderId = order1.getOrderId();
        Order orderToCancel = order1.toBuilder()
                .status(OrderStatus.CREATED)
                .build();

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
    @DisplayName("Cancel order - negative case (order not found)")
    void cancelOrder_NonExistingOrder_ThrowsException() {

        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.cancel(orderId));
        assertEquals("Order with id " + orderId + " not found", exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
    }
}