package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.exception.OrderItemNotFoundException;
import de.telran.gardenStore.repository.OrderItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest extends AbstractTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    @DisplayName("Get order item : positive case")
    void getByIdPositiveCase() {
        AppUser currentUser = user1;
        OrderItem expectedOrderItem = orderItem1;
        Long orderItemId = expectedOrderItem.getOrderItemId();

        when(userService.getCurrent()).thenReturn(currentUser);
        when(orderItemRepository.findByUserAndId(currentUser, orderItemId))
                .thenReturn(Optional.of(expectedOrderItem));

        OrderItem result = orderItemService.getById(orderItemId);

        assertNotNull(result);
        assertEquals(orderItemId, result.getOrderItemId());
        verify(userService).getCurrent();
        verify(orderItemRepository).findByUserAndId(currentUser, orderItemId);
    }

    @Test
    @DisplayName("Get order item : negative case")
    void getByIdNegativeCase() {
        AppUser currentUser = user1;
        Long invalidId = 999L;

        when(userService.getCurrent()).thenReturn(currentUser);
        when(orderItemRepository.findByUserAndId(currentUser, invalidId))
                .thenReturn(Optional.empty());

        OrderItemNotFoundException exception = assertThrows(
                OrderItemNotFoundException.class,
                () -> orderItemService.getById(invalidId)
        );

        assertTrue(exception.getMessage().contains("Order item with id " + invalidId + " not found"));
        verify(userService).getCurrent();
        verify(orderItemRepository).findByUserAndId(currentUser, invalidId);
    }
}

