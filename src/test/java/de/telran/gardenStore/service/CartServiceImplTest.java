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
    void getByUserNegativeCase() {
        AppUser user = user1;

        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(CartNotFoundException.class,
                () -> cartService.getByUser(user));

        assertEquals("Cart for user " + user.getUserId() + " not found", exception.getMessage());
        verify(cartRepository).findByUser(user);
    }

    @DisplayName("Create cart")
    @Test
    void create() {
        AppUser user = user1;
        Cart cartToCreate = Cart.builder().user(user).build();
        Cart expected = cartToCreate.toBuilder()
                .cartId(1L)
                .build();

        when(cartRepository.save(cartToCreate)).thenReturn(expected);

        Cart actual = cartService.create(user);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertTrue(actual.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @DisplayName("Update cart")
    @Test
    void update() {
        AppUser user = user1;
        Cart cart = cart1;

        Cart expected = cart1.toBuilder()
                .items(new ArrayList<>())
                .build();

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(expected)).thenReturn(expected);

        Cart actual = cartService.update(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
        assertTrue(actual.getItems().isEmpty());
        verify(cartRepository).save(expected);
    }

    @DisplayName("Add item to cart : new cart")
    @Test
    void addItemNewCart() {
        AppUser user = user1;
        Long productId = product3.getProductId();
        Product product = product3;
        Integer quantity = 1;
        Cart cartToCreate = Cart.builder().user(user).build();
        Cart cartCreated = cartToCreate.toBuilder().cartId(1L).build();

        Cart cartToUpdate = cartCreated.toBuilder().build();

        CartItem cartItemCreated = CartItem.builder()
                .product(product)
                .quantity(quantity)
                .build();

        cartToUpdate.setItems((new ArrayList<>(List.of(cartItemCreated))));

        CartItem cartItemSaved = cartItemCreated.toBuilder()
                .cartItemId(4L)
                .build();

        Cart expected = cartToUpdate.toBuilder()
                .items(new ArrayList<>(List.of(cartItemSaved)))
                .build();

        when(userService.getCurrent()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(cartToCreate)).thenReturn(cartCreated);
        when(productService.getById(productId)).thenReturn(product);
        when(cartRepository.save(cartToUpdate)).thenReturn(expected);

        Cart actual = cartService.addItem(productId);

        assertEquals(expected, actual);
        verify(cartRepository).save(expected);
    }

    @DisplayName("Add item to cart : existing cart")
    @Test
    void addItemExistingCart() {
        AppUser user = user1;
        Long productId = product3.getProductId();
        Product product = product3;
        Cart cart = cart1;

        Cart cartToSave = cart1.toBuilder().build();
        CartItem cartItemCreated = CartItem.builder()
                .product(product)
                .quantity(2)
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

        cartService.addItem(productId);
        Cart actual = cartService.addItem(productId);

        assertEquals(expected, actual);
    }

    @DisplayName("Update cart item")
    @Test
    void updateItem() {
        AppUser user = user1;
        Long cartItemId = cartItem1.getCartItemId();
        Integer quantityUpdated = 5;
        CartItem updatedItem =cartItem1.toBuilder().quantity(quantityUpdated).build();
        Cart cart = cart1;
        Cart expected = cart1.toBuilder().build();
        cart1.getItems().remove(cartItem1);
        cart1.getItems().add(updatedItem);

        when(userService.getCurrent()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(expected)).thenReturn(expected);

        Cart actual = cartService.updateItem(cartItemId, quantityUpdated);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartRepository).save(expected);
    }

    @DisplayName("Delete cart item")
    @Test
    void deleteItem() {
        AppUser user = user1;
        Long cartItemId = cartItem1.getCartItemId();
        Cart cart = cart1;
        Cart expected = cart1.toBuilder()
                .items(new ArrayList<>(cart.getItems()))
                .build();
        expected.getItems().remove(cartItem1);

        when(userService.getCurrent()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(expected)).thenReturn(expected);

        Cart actual = cartService.deleteItem(cartItemId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartRepository).save(expected);
    }
}

