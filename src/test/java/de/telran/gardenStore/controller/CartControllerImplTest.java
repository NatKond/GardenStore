package de.telran.gardenStore.controller;

import de.telran.gardenStore.converter.ConverterEntityToDtoShort;
import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.dto.CartResponseDto;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.enums.Role;
import de.telran.gardenStore.service.CartService;
import de.telran.gardenStore.service.UserService;
import de.telran.gardenStore.service.security.JwtAuthFilter;
import de.telran.gardenStore.service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    @MockBean
    private ConverterEntityToDtoShort<Cart, CartResponseDto> cartConverter;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    private AppUser user1;
    private Product product1;
    private Product product2;
    private Cart cart1;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private CartResponseDto cartResponseDto;

    @BeforeEach
    void setupTestData() {
        user1 = AppUser.builder()
                .userId(1L)
                .name("Test User")
                .email("test@example.com")
                .passwordHash("123")
                .role(Role.ROLE_USER)
                .build();

        product1 = Product.builder()
                .productId(1L)
                .name("Fertilizer")
                .price(BigDecimal.valueOf(10))
                .discountPrice(BigDecimal.valueOf(8))
                .build();

        product2 = Product.builder()
                .productId(2L)
                .name("Compost")
                .price(BigDecimal.valueOf(15))
                .discountPrice(BigDecimal.valueOf(12))
                .build();

        cart1 = Cart.builder()
                .cartId(1L)
                .user(user1)
                .build();

        cartItem1 = CartItem.builder()
                .cart(cart1)
                .product(product1)
                .quantity(2)
                .build();

        cartItem2 = CartItem.builder()
                .cart(cart1)
                .product(product2)
                .quantity(1)
                .build();

        cart1.setItems(List.of(cartItem1, cartItem2));

        CartItemResponseDto item1 = CartItemResponseDto.builder()
                .cartItemId(100L)
                .product(ProductShortResponseDto.builder()
                        .productId(product1.getProductId())
                        .name(product1.getName())
                        .price(product1.getPrice())
                        .discountPrice(product1.getDiscountPrice())
                        .build())
                .quantity(cartItem1.getQuantity())
                .build();

        CartItemResponseDto item2 = CartItemResponseDto.builder()
                .cartItemId(101L)
                .product(ProductShortResponseDto.builder()
                        .productId(product2.getProductId())
                        .name(product2.getName())
                        .price(product2.getPrice())
                        .discountPrice(product2.getDiscountPrice())
                        .build())
                .quantity(cartItem2.getQuantity())
                .build();

        cartResponseDto = CartResponseDto.builder()
                .cartId(cart1.getCartId())
                .userId(user1.getUserId())
                .items(List.of(item1, item2))
                .build();
    }

    @Test
    void testGetCartForCurrentUser() throws Exception {
        when(userService.getCurrent()).thenReturn(user1);
        when(cartService.getByUser(user1)).thenReturn(cart1);
        when(cartConverter.convertEntityToDto(cart1)).thenReturn(cartResponseDto);

        mockMvc.perform(get("/v1/cart")
                        .with(user(user1.getEmail()).password("123").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(cart1.getCartId()))
                .andExpect(jsonPath("$.userId").value(user1.getUserId()));
    }

    @Test
    void testAddCartItem() throws Exception {
        Long productId = product1.getProductId();

        when(cartService.addCartItem(productId)).thenReturn(cart1);
        when(cartConverter.convertEntityToDto(cart1)).thenReturn(cartResponseDto);

        assertNotNull(cartResponseDto, "cartResponseDto is null — значит мок не сработал");

        mockMvc.perform(post("/v1/cart/items")
                        .param("productId", productId.toString())
                        .with(csrf())
                        .with(user(user1.getEmail()).password("123").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(cart1.getCartId()))
                .andExpect(jsonPath("$.userId").value(user1.getUserId()))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].product.name").value("Fertilizer"))
                .andExpect(jsonPath("$.items[1].product.name").value("Compost"));
    }

    @Test
    void testUpdateCartItem() throws Exception {
        Long cartItemId = 100L;
        Integer quantity = 3;

        when(cartService.updateCartItem(cartItemId, quantity)).thenReturn(cart1);
        when(cartConverter.convertEntityToDto(cart1)).thenReturn(cartResponseDto);

        mockMvc.perform(put("/v1/cart/items/{cartItemId}", cartItemId)
                        .param("quantity", quantity.toString())
                        .with(csrf())
                        .with(user(user1.getEmail()).password("123").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.cartId").value(cart1.getCartId()));
    }

    @Test
    void testDeleteCartItem() throws Exception {
        Long cartItemId = 100L;

        when(cartService.deleteCartItem(cartItemId)).thenReturn(cart1);
        when(cartConverter.convertEntityToDto(cart1)).thenReturn(cartResponseDto);

        mockMvc.perform(delete("/v1/cart/items/{cartItemId}", cartItemId)
                        .with(csrf())
                        .with(user(user1.getEmail()).password("123").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(cart1.getCartId()));
    }
}







