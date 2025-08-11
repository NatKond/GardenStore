package de.telran.gardenStore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.CartItemResponseDto;
import de.telran.gardenStore.dto.CartResponseDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class CartIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v1/cart - Get cart for current user")
    void getForCurrentUser() throws Exception {

        mockMvc.perform(get("/v1/cart")
                        .with(httpBasic("alice.johnson@example.com", "12345")))
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

        CartItemResponseDto cartItemResponseDtoUpdated = cartItemResponseDto1.toBuilder()
                .quantity(cartItemResponseDto1.getQuantity() + 1)
                .build();

        CartResponseDto cartResponseDtoUpdated = cartResponseDto1.toBuilder().build();

        cartResponseDtoUpdated.getItems().remove(cartItemResponseDto1);
        cartResponseDtoUpdated.getItems().add(cartItemResponseDtoUpdated);

        mockMvc.perform(post("/v1/cart/items/{productId}", productId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
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

        CartItemResponseDto cartItemResponseDtoUpdated = cartItemResponseDto1.toBuilder()
                .quantity(quantity)
                .build();

        CartResponseDto cartResponseDtoUpdated = cartResponseDto1.toBuilder().build();

        cartResponseDtoUpdated.getItems().remove(cartItemResponseDto1);
        cartResponseDtoUpdated.getItems().add(cartItemResponseDtoUpdated);

        mockMvc.perform(put("/v1/cart/items/{cartItemId}", cartItemId)
                        .param("quantity", quantity.toString())
                        .with(httpBasic("alice.johnson@example.com", "12345")))
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

        CartResponseDto cartResponseDtoUpdated = cartResponseDto1.toBuilder().build();
        cartResponseDtoUpdated.getItems().remove(cartItemResponseDto1);

        mockMvc.perform(delete("/v1/cart/items/{cartItemId}", cartItemId)
                .with(httpBasic("alice.johnson@example.com", "12345")))
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

        mockMvc.perform(delete("/v1/cart/items/{cartItemId}", cartItemId)
                .with(httpBasic("alice.johnson@example.com", "12345")))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("CartItemNotFoundException"),
                        jsonPath("$.message").value("CartItem with id " + cartItemId + " not found"));
    }

}
