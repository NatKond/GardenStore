package de.telran.gardenStore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.Order;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class OrderIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v1/orders/history - Get order history for current user")
    void getAllPositiveCase() throws Exception {
        List<OrderShortResponseDto> expected = List.of(orderShortResponseDto1, orderShortResponseDto3);


        mockMvc.perform(get("/v1/orders/history")
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/orders/history/delivered- Get all delivered orders history for current user")
    void getAllDelivered() throws Exception {
        List<OrderResponseDto> expected = List.of(orderResponseDto3);

        mockMvc.perform(get("/v1/orders/history/delivered")
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("GET /v1/orders/{orderId} - Get order by ID : positive case")
    void getOrderByIdPositiveCase() throws Exception {

        mockMvc.perform(get("/v1/orders/{orderId}", order1.getOrderId())
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(orderResponseDto1), JsonCompareMode.LENIENT));
    }

    @Test
    @DisplayName("GET /v1/orders/{orderId} - Get order by ID : negative case ")
    void getOrderByIdNegativeCase() throws Exception {
        Long nonExistentId = 2L;

        mockMvc.perform(get("/v1/orders/{orderId}", nonExistentId)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpect(status().isNotFound())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("OrderNotFoundException"),
                        jsonPath("$.message").value("Order with id " + nonExistentId + " not found"));
    }

    @Test
    @DisplayName("POST /v1/orders/{userId} - Create new order : positive case")
    void createPositiveCase() throws Exception {
        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequestDto))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.orderId").value(orderResponseCreatedDto.getOrderId()),
                        jsonPath("$.userId").value(orderResponseCreatedDto.getUserId()),
                        jsonPath("$.status").value(orderResponseCreatedDto.getStatus()),
                        jsonPath("$.deliveryAddress").value(orderResponseCreatedDto.getDeliveryAddress()),
                        jsonPath("$.contactPhone").value(orderResponseCreatedDto.getContactPhone()),
                        jsonPath("$.deliveryMethod").value(orderResponseCreatedDto.getDeliveryMethod()),
                        jsonPath("$.items.length()").value(orderResponseCreatedDto.getItems().size()),
                        jsonPath("$.items[0].orderItemId").value(orderResponseCreatedDto.getItems().getFirst().getOrderItemId()),
                        jsonPath("$.items[0].product.productId").value(orderResponseCreatedDto.getItems().getFirst().getProduct().getProductId()),
                        jsonPath("$.items[0].quantity").value(orderResponseCreatedDto.getItems().getFirst().getQuantity()),
                        jsonPath("$.items[0].priceAtPurchase").value(orderResponseCreatedDto.getItems().getFirst().getPriceAtPurchase()),
                        jsonPath("$.items[1].orderItemId").value(orderResponseCreatedDto.getItems().get(1).getOrderItemId()),
                        jsonPath("$.items[1].product.productId").value(orderResponseCreatedDto.getItems().get(1).getProduct().getProductId()),
                        jsonPath("$.items[1].quantity").value(orderResponseCreatedDto.getItems().get(1).getQuantity()),
                        jsonPath("$.items[1].priceAtPurchase").value(orderResponseCreatedDto.getItems().get(1).getPriceAtPurchase()),
                        jsonPath("$.totalAmount").value(orderResponseCreatedDto.getTotalAmount()));
    }

    @Test
    @DisplayName("POST /v1/orders/{userId} - Create new order : negative case")
    void createNegativeCase() throws Exception {
        List<OrderItemCreateRequestDto> orderItemsCreateRequestDto = List.of(
                OrderItemCreateRequestDto.builder()
                        .productId(product3.getProductId())
                        .quantity(3)
                        .build());

        OrderCreateRequestDto orderInvalidCreateRequestDto = orderCreateRequestDto.toBuilder()
                .items(orderItemsCreateRequestDto)
                .build();

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderInvalidCreateRequestDto))
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("EmptyOrderException"),
                        jsonPath("$.message").value("Order is empty"));

    }

    @Test
    @DisplayName("POST /v1/orders/items?orderId={orderId}&productId={productId}&quantity={quantity} - Update order item")
    void addItem() throws Exception {
        Order order = order2;
        Integer quantity = 2;

        Long productId = product3.getProductId();
        OrderItemResponseDto orderItemResponseDto = orderItemResponseDto3.toBuilder()
                .quantity(quantity + orderItemResponseDto3.getQuantity())
                .build();

        List<OrderItemResponseDto> orderItemResponseDtos = new ArrayList<>(List.of(orderItemResponseDto));
        OrderResponseDto expected = orderResponseDto2.toBuilder()
                .items(orderItemResponseDtos)
                .totalAmount(orderItemResponseDto.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItemResponseDto.getQuantity())))
                .build();

        mockMvc.perform(post("/v1/orders/items?orderId={orderId}&productId={productId}&quantity={quantity}",
                        order.getOrderId(),
                        productId,
                        quantity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("bob.smith@example.com", "12345")))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("PUT /v1/orders/items?orderItemId={orderItemId}&quantity={quantity} - Update order item")
    void updateItem() throws Exception {
        Integer quantity = 3;

        Long orderItemId = orderItem3.getOrderItemId();
        OrderItemResponseDto orderItemResponseDto = orderItemResponseDto3.toBuilder()
                .quantity(quantity)
                .build();

        List<OrderItemResponseDto> orderItemResponseDtos = new ArrayList<>(List.of(orderItemResponseDto));
        OrderResponseDto expected = orderResponseDto2.toBuilder()
                .items(orderItemResponseDtos)
                .totalAmount(orderItemResponseDto.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItemResponseDto.getQuantity())))
                .build();

        mockMvc.perform(put("/v1/orders/items?orderItemId={orderItemId}&quantity={quantity}",
                        orderItemId,
                        quantity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("bob.smith@example.com", "12345")))
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("DELETE /v1/orders/items/{orderItemId} - Delete order item : negative case")
    void deleteItem() throws Exception {
        Long orderItemId = orderItem3.getOrderItemId();

        mockMvc.perform(delete("/v1/orders/items/{orderItemId} ", orderItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("bob.smith@example.com", "12345")))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("EmptyOrderException"),
                        jsonPath("$.message").value("Order is empty"),
                        jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("DELETE /v1/orders/{orderId} - Cancel order : positive case")
    void cancelOrderPositiveCase() throws Exception {
        Long orderId = order1.getOrderId();

        OrderResponseDto orderCanceledResponseDto = OrderResponseDto.builder()
                .orderId(orderId)
                .status("CANCELLED")
                .build();

        mockMvc.perform(delete("/v1/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(orderCanceledResponseDto)));
    }

    @Test
    @DisplayName("DELETE /v1/orders/{orderId} - Cancel order : negative case")
    void cancelOrderNegativeCase() throws Exception {
        Long orderWithIncorrectStatus = 3L;

        mockMvc.perform(delete("/v1/orders/{orderId}", orderWithIncorrectStatus)
                        .with(httpBasic("alice.johnson@example.com", "12345")))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("OrderCancellationException"),
                        jsonPath("$.message").value("Order cannot be cancelled in current status " + order3.getStatus()),
                        jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
    }
}
