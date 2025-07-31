package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.exception.CartNotFoundException;
import de.telran.gardenStore.repository.CartRepository;
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
class CartServiceImplTest extends AbstractTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    @DisplayName("Get cart by user : positive case")
    @Test
    void getByUserPositiveCase() {

        AppUser user = user1;
        Cart expected = user.getCart();

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(expected));

        Cart actual = cartService.getByUser(user);


        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartRepository).findByUser(user);
    }

    @DisplayName("Get cart by user : negative case")
    @Test
    void getByUser_WhenCartNotExists_ThrowsException() {

        AppUser user = user1;

        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(CartNotFoundException.class,
                () -> cartService.getByUser(user));

        assertEquals("Cart for user " + user.getUserId() + " not found", exception.getMessage());
        verify(cartRepository).findByUser(user);
    }

    @DisplayName("Create cart : positive case")
    @Test
    void createCartPositiveCase() {

        AppUser user = user1;
        Cart expected = Cart.builder().user(user).build();

        when(cartRepository.save(any(Cart.class))).thenReturn(expected);

        Cart actual = cartService.create(user);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertTrue(actual.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @DisplayName("Add item to cart")
    @Test
    void addCartItem() {
        AppUser user = user1;
        Long productId = product3.getProductId();
        Product product = product3;
        Cart cart = cart1;

        Cart cartToSave = cart1.toBuilder().build();
        CartItem cartItemCreated = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(1)
                .build();

        List<CartItem> cartItemsToSave = new ArrayList<>(cartToSave.getItems());
        cartItemsToSave.add(cartItemCreated);
        cartToSave.setItems(cartItemsToSave);

        CartItem cartItemSaved = cartItemCreated.toBuilder()
                .cartItemId(4L)
                .build();

        Cart expected = cart1.toBuilder().build();
        List<CartItem> cartItemsSaved = new ArrayList<>(cartToSave.getItems());
        cartItemsSaved.add(cartItemSaved);
        expected.setItems(cartItemsSaved);

        when(userService.getCurrent()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productService.getById(productId)).thenReturn(product);
        when(cartRepository.save(cartToSave)).thenReturn(expected);

        Cart actual = cartService.addCartItem(productId);

        assertEquals(expected, actual);
        verify(cartRepository).save(expected);
    }


    @DisplayName("Update cart item")
    @Test
    void updateCartItem() {

        Long cartItemId = cartItem1.getCartItemId();
        Integer quantityUpdated = 5;
        CartItem updatedItem =cartItem1.toBuilder().quantity(quantityUpdated).build();

        Cart expected = cart1.toBuilder().build();
        cart1.getItems().remove(cartItem1);
        cart1.getItems().add(updatedItem);

        when(cartItemService.getById(cartItemId)).thenReturn(cartItem1);
        when(userService.getCurrent()).thenReturn(user1);
        when(cartRepository.save(expected)).thenReturn(expected);

        Cart actual = cartService.updateCartItem(cartItemId, quantityUpdated);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartRepository).save(expected);
    }

    @DisplayName("Delete cart item")
    @Test
    void deleteCartItem() {
        Long cartItemId = cartItem1.getCartItemId();
        Cart expected = cart1.toBuilder().build();
        cart1.getItems().remove(cartItem1);

        when(cartItemService.getById(cartItemId)).thenReturn(cartItem1);
        when(userService.getCurrent()).thenReturn(user1);
        when(cartRepository.save(expected)).thenReturn(expected);

        Cart actual = cartService.deleteCartItem(cartItemId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartRepository).save(expected);
    }
}

