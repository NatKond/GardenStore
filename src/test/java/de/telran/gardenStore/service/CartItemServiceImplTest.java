package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.exception.CartItemNotFoundException;
import de.telran.gardenStore.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest extends AbstractTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    void getById_validId_shouldReturnCartItem() {

        AppUser currentUser = user1;
        CartItem expectedCartItem = cartItem1;
        Long cartItemId = expectedCartItem.getCartItemId();

        when(userService.getCurrent()).thenReturn(currentUser);
        when(cartItemRepository.findByUserAndId(currentUser, cartItemId))
                .thenReturn(Optional.of(expectedCartItem));


        CartItem result = cartItemService.getById(cartItemId);


        assertNotNull(result);
        assertEquals(cartItemId, result.getCartItemId());
        verify(userService).getCurrent();
        verify(cartItemRepository).findByUserAndId(currentUser, cartItemId);
    }

    @Test
    void getById_invalidId_shouldThrowCartItemNotFoundException() {

        AppUser currentUser = user1;
        Long invalidId = 888L;

        when(userService.getCurrent()).thenReturn(currentUser);
        when(cartItemRepository.findByUserAndId(currentUser, invalidId))
                .thenReturn(Optional.empty());


        CartItemNotFoundException exception = assertThrows(
                CartItemNotFoundException.class,
                () -> cartItemService.getById(invalidId)
        );

        assertTrue(exception.getMessage().contains("CartItem with id " + invalidId + " not found"));
        verify(userService).getCurrent();
        verify(cartItemRepository).findByUserAndId(currentUser, invalidId);
    }
}

