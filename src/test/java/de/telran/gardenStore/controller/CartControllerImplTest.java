package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.ConverterEntityToDtoShort;
import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.dto.CartResponseDto;
import de.telran.gardenStore.entity.Cart;
import de.telran.gardenStore.entity.CartItem;
import de.telran.gardenStore.exception.CartItemNotFoundException;
import de.telran.gardenStore.service.CartService;
import de.telran.gardenStore.service.UserService;
import de.telran.gardenStore.service.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CartControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ConverterEntityToDtoShort<Cart, CartResponseDto> cartConverter;

    @Test
    @DisplayName("GET /v1/cart - Get cart for current user")
    void getForCurrentUser() throws Exception {
        when(userService.getCurrent()).thenReturn(user1);
        when(cartService.getByUser(user1)).thenReturn(cart1);
        when(cartConverter.convertEntityToDto(cart1)).thenReturn(cartResponseDto1);

        mockMvc.perform(get("/v1/cart"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(cartResponseDto1)));
    }

    @Test
    @DisplayName("POST /v1/cart/items/{productId} - Add cart item for current user")
    void addItem() throws Exception {
        Long productId = product1.getProductId();

        CartItem cartItemUpdated = cartItem1.toBuilder()
                .quantity(cartItem1.getQuantity() + 1)
                .build();

        Cart cartUpdated = cart1.toBuilder().build();

        cartUpdated.getItems().remove(cartItem1);
        cartUpdated.getItems().add(cartItemUpdated);

        CartItemResponseDto cartItemResponseDtoUpdated = cartItemResponseDto1.toBuilder()
                .quantity(cartItemUpdated.getQuantity())
                .build();

        CartResponseDto cartResponseDtoUpdated = cartResponseDto1.toBuilder().build();

        cartResponseDtoUpdated.getItems().remove(cartItemResponseDto1);
        cartResponseDtoUpdated.getItems().add(cartItemResponseDtoUpdated);

        when(cartService.addItem(productId)).thenReturn(cartUpdated);
        when(cartConverter.convertEntityToDto(cartUpdated)).thenReturn(cartResponseDtoUpdated);


        mockMvc.perform(post("/v1/cart/items/{productId}", productId))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(cartResponseDtoUpdated)));
    }

    @Test
    @DisplayName("PUT /v1/cart/items/{cartItemId}?quantity={quantity} - Update cart item for current user")
    void updateItem() throws Exception {
        Long cartItemId = cart1.getCartId();

        Integer quantity = 3;

        CartItem cartItemUpdated = cartItem1.toBuilder()
                .quantity(quantity)
                .build();

        Cart cartUpdated = cart1.toBuilder().build();

        cartUpdated.getItems().remove(cartItem1);
        cartUpdated.getItems().add(cartItemUpdated);

        CartItemResponseDto cartItemResponseDtoUpdated = cartItemResponseDto1.toBuilder()
                .quantity(cartItemUpdated.getQuantity())
                .build();

        CartResponseDto cartResponseDtoUpdated = cartResponseDto1.toBuilder().build();

        cartResponseDtoUpdated.getItems().remove(cartItemResponseDto1);
        cartResponseDtoUpdated.getItems().add(cartItemResponseDtoUpdated);

        when(cartService.updateItem(cartItemId, quantity)).thenReturn(cartUpdated);
        when(cartConverter.convertEntityToDto(cart1)).thenReturn(cartResponseDtoUpdated);

        mockMvc.perform(put("/v1/cart/items/{cartItemId}", cartItemId)
                        .param("quantity", quantity.toString()))
                .andDo(print())
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(cartResponseDtoUpdated)));
    }

    @Test
    @DisplayName("DELETE /v1/cart/items/{cartItemId} - Delete cart item for current user")
    void deleteItemPositiveCase() throws Exception {
        Long cartItemId = cartItem1.getCartItemId();

        Cart cartUpdated = cart1.toBuilder().build();
        cartUpdated.getItems().remove(cartItem1);

        CartResponseDto cartResponseDtoUpdated = cartResponseDto1.toBuilder().build();
        cartResponseDtoUpdated.getItems().remove(cartItemResponseDto1);

        when(cartService.deleteItem(cartItemId)).thenReturn(cartUpdated);
        when(cartConverter.convertEntityToDto(cartUpdated)).thenReturn(cartResponseDtoUpdated);

        mockMvc.perform(delete("/v1/cart/items/{cartItemId}", cartItemId))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(cartResponseDtoUpdated)));
    }

    @Test
    @DisplayName("DELETE /v1/cart/items/{cartItemId} - Delete cart item for current user")
    void deleteItemNegativeCase() throws Exception {
        Long cartItemId = 99L;

        when(cartService.deleteItem(cartItemId)).thenReturn(cart1);
        when(cartConverter.convertEntityToDto(cart1)).thenThrow(new CartItemNotFoundException("CartItem with id " + cartItemId + " not found"));

        mockMvc.perform(delete("/v1/cart/items/{cartItemId}", cartItemId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("CartItemNotFoundException"),
                        jsonPath("$.message").value("CartItem with id " + cartItemId + " not found"));
    }
}