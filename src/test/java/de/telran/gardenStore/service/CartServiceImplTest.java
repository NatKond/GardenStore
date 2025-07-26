package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.Role;
import de.telran.gardenStore.exception.CartNotFoundException;
import de.telran.gardenStore.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest extends AbstractTest {

    @Mock
    private CartRepository cartRepositoryMock;

    @Mock
    private CartItemService cartItemServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private ProductService productServiceMock;

    @InjectMocks
    private CartServiceImpl cartService;
    private AppUser testUser = new AppUser(1L,
            "Ivan Ivanov",
            "test@user.com",
            "0159785634",
            "123",
            Role.ROLE_USER,
            new ArrayList<>(),
            new Cart());
    private Product testProduct = new Product(1L,
            "Test Product",
            BigDecimal.valueOf(10.0),
            BigDecimal.valueOf(10.0),
            "",
            "",
            LocalDateTime.now(),
            LocalDateTime.now(),
            new Category());

    @DisplayName("Get cart by user - positive case (cart exists)")
    @Test
    void getByUser_WhenCartExists_ReturnsCart() {


        AppUser user = testUser;
        Cart expectedCart = user.getCart();

        when(cartRepositoryMock.findByUser(user)).thenReturn(Optional.of(expectedCart));


        Cart result = cartService.getByUser(user);


        assertNotNull(result);
        assertEquals(expectedCart.getCartId(), result.getCartId());
        verify(cartRepositoryMock, times(1)).findByUser(user);
    }
    @DisplayName("Get cart by user - negative case (cart not found)")
    @Test
    void getByUser_WhenCartNotExists_ThrowsException() {

        AppUser user = testUser;

        when(cartRepositoryMock.findByUser(user)).thenReturn(Optional.empty());


        CartNotFoundException exception = assertThrows(CartNotFoundException.class,
                () -> cartService.getByUser(user));

        assertEquals("Cart for user " + user.getUserId() + " not found", exception.getMessage());
        verify(cartRepositoryMock, times(1)).findByUser(user);
    }
    @DisplayName("Create cart - creates new cart for user")
    @Test
    void createCart_WithValidUser_CreatesNewCart() {

        AppUser user = testUser;
        Cart newCart = new Cart(1L, user, new ArrayList<>());

        when(cartRepositoryMock.save(any(Cart.class))).thenReturn(newCart);


        Cart result = cartService.create(user);


        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertTrue(result.getItems().isEmpty());
        verify(cartRepositoryMock, times(1)).save(any(Cart.class));
    }
    @DisplayName("Add item to cart - adds new item when not exists")
    @Test
    void addCartItem_WhenItemNotExists_AddsNewItem() {

        Long userId = 1L;
        Long productId = 1L;
        AppUser user = testUser;
        Product product = testProduct;
        Cart cart = new Cart(1L, user, new ArrayList<>());

        when(userServiceMock.getUserById(userId)).thenReturn(user);
        when(cartRepositoryMock.findByUser(user)).thenReturn(Optional.of(cart));
        when(productServiceMock.getProductById(productId)).thenReturn(product);
        when(cartRepositoryMock.save(cart)).thenReturn(cart);


        Cart result = cartService.addCartItem(userId, productId);


        assertEquals(1, result.getItems().size());
        assertEquals(product, result.getItems().get(0).getProduct());
        assertEquals(1, result.getItems().get(0).getQuantity());
        verify(cartRepositoryMock, times(1)).save(cart);
    }
    @DisplayName("Add item to cart - increments quantity when exists")
    @Test
    void addCartItem_WhenItemExists_IncrementsQuantity() {

        Long userId = 1L;
        Long productId = 1L;
        AppUser user = testUser;
        Product product = testProduct;
        CartItem existingItem = new CartItem(1L, null, product, 1);
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(existingItem);
        Cart cart = new Cart(1L, user, cartItemList);

        when(userServiceMock.getUserById(userId)).thenReturn(user);
        when(cartRepositoryMock.findByUser(user)).thenReturn(Optional.of(cart));
//        when(productServiceMock.getProductById(productId)).thenReturn(product);
        when(cartRepositoryMock.save(cart)).thenReturn(cart);


        Cart result = cartService.addCartItem(userId, productId);


        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getItems().get(0).getQuantity());
        verify(cartRepositoryMock, times(1)).save(cart);
    }
    @DisplayName("Update cart item - updates quantity")
    @Test
    void updateCartItem_WithValidQuantity_UpdatesItem() {

        Long cartItemId = 1L;
        Integer newQuantity = 5;
        CartItem updatedItem = new CartItem(cartItemId, null, null, newQuantity);

        when(cartItemServiceMock.update(cartItemId, newQuantity)).thenReturn(updatedItem);


        Cart result = cartService.updateCartItem(cartItemId, newQuantity);


        assertNotNull(result);
        verify(cartItemServiceMock, times(1)).update(cartItemId, newQuantity);
    }
    @DisplayName("Delete cart item - removes item from cart")
    @Test
    void deleteCartItem_WithValidId_RemovesItem() {

        Long cartItemId = 1L;
        doNothing().when(cartItemServiceMock).delete(cartItemId);

        cartService.deleteCartItem(cartItemId);

        verify(cartItemServiceMock, times(1)).delete(cartItemId);
    }
}

