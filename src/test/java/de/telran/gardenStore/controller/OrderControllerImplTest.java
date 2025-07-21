package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.handler.GlobalExceptionHandler;
import de.telran.gardenStore.service.CartService;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class OrderControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CartService cartService;
    @Test
    @DisplayName("GET /v1/orders/history/{userId} - Get order history for user")
    void getOrderHistoryForUser() throws Exception {
        List<Order> userOrders = List.of(order1);
        when(orderService.getAllByUserId(user1.getUserId())).thenReturn(userOrders);
        when(orderConverter.convertEntityListToDtoList(userOrders)).thenReturn(List.of(orderShortResponseDto1));

        mockMvc.perform(get("/v1/orders/history/{userId}", user1.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].orderId").value(order1.getOrderId()))
                .andExpect(jsonPath("$[0].status").value(order1.getStatus().name()));

        verify(orderService).getAllByUserId(user1.getUserId());
        verify(orderConverter).convertEntityListToDtoList(userOrders);
    }

    @Test
    @DisplayName("GET /v1/orders/{orderId} - Get order by ID (success)")
    void getOrderById_Success() throws Exception {
        when(orderService.getById(order1.getOrderId())).thenReturn(order1);
        when(orderConverter.convertEntityToDto(order1)).thenReturn(orderResponseDto1);

        mockMvc.perform(get("/v1/orders/{orderId}", order1.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(order1.getOrderId()))
                .andExpect(jsonPath("$.status").value(order1.getStatus().name()))
                .andExpect(jsonPath("$.items.length()").value(2));

        verify(orderService).getById(order1.getOrderId());
        verify(orderConverter).convertEntityToDto(order1);
    }

    @Test
    @DisplayName("GET /v1/orders/{orderId} - Order not found")
    void getOrderById_NotFound() throws Exception {
        Long nonExistentId = 999L;
        when(orderService.getById(nonExistentId))
                .thenThrow(new OrderNotFoundException("Order with id " + nonExistentId + " not found"));

        mockMvc.perform(get("/v1/orders/{orderId}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("OrderNotFoundException"))
                .andExpect(jsonPath("$.message").value("Order with id " + nonExistentId + " not found"));

        verify(orderService).getById(nonExistentId);
    }

    @Test
    @DisplayName("POST /v1/orders/{userId} - Create new order (success)")
    void createOrder_Success() throws Exception {
        Order newOrder = order1.toBuilder()
                .orderId(null)
                .user(null)
                .build();

        Order savedOrder = newOrder.toBuilder()
                .orderId(3L)
                .user(user1)
                .build();

        OrderResponseDto expectedResponse = orderResponseCreatedDto.toBuilder()
                .orderId(3L)
                .status(OrderStatus.CREATED.name())
                .build();

        when(userService.getUserById(user1.getUserId())).thenReturn(user1);
        when(orderConverter.convertDtoToEntity(orderCreateRequestDto)).thenReturn(newOrder);
        when(orderService.create(argThat(order ->
                order.getUser().equals(user1) &&
                        order.getItems().size() == newOrder.getItems().size()
        ))).thenReturn(savedOrder);
        when(orderConverter.convertEntityToDto(savedOrder)).thenReturn(expectedResponse);

        mockMvc.perform(post("/v1/orders/{userId}", user1.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(3L))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.items.length()").value(2));

        verify(userService).getUserById(user1.getUserId());
        verify(orderConverter).convertDtoToEntity(orderCreateRequestDto);
        verify(orderService).create(any(Order.class));
        verify(orderConverter).convertEntityToDto(savedOrder);
    }


    @Test
    @DisplayName("POST /v1/orders/{userId} - Invalid request (empty items)")
    void createOrder_EmptyItems_ValidationError() throws Exception {
        OrderCreateRequestDto invalidRequest = orderCreateRequestDto.toBuilder()
                .items(List.of())
                .build();

        mockMvc.perform(post("/v1/orders/{userId}", user1.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.messages.items").value("must not be empty"));
    }

    @Test
    @DisplayName("DELETE /v1/orders/{orderId} - Cancel order (success)")
    void cancelOrder_Success() throws Exception {
        Order cancelledOrder = order1.toBuilder()
                .status(OrderStatus.CANCELLED)
                .build();

        OrderResponseDto cancelledResponse = orderResponseDto1.toBuilder()
                .status(OrderStatus.CANCELLED.name())
                .build();

        when(orderService.cancel(order1.getOrderId())).thenReturn(cancelledOrder);
        when(orderConverter.convertEntityToDto(cancelledOrder)).thenReturn(cancelledResponse);

        mockMvc.perform(delete("/v1/orders/{orderId}", order1.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(orderService).cancel(order1.getOrderId());
        verify(orderConverter).convertEntityToDto(cancelledOrder);
    }
    @Test
    @DisplayName("GET /v1/orders - Get all orders with filtering by status")
    void getAllOrders_WithStatusFilter() throws Exception {
        List<Order> filteredOrders = List.of(order1);
        when(orderService.getAllOrders(OrderStatus.PAID, null, null)).thenReturn(filteredOrders);
        when(orderConverter.convertEntityListToDtoList(filteredOrders)).thenReturn(List.of(orderShortResponseDto1));

        mockMvc.perform(get("/v1/orders")
                        .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].orderId").value(order1.getOrderId()))
                .andExpect(jsonPath("$[0].status").value("PAID"));

        verify(orderService).getAllOrders(OrderStatus.PAID, null, null);
        verify(orderConverter).convertEntityListToDtoList(filteredOrders);
    }

    @Test
    @DisplayName("GET /v1/orders - Get all orders with date range filter")
    void getAllOrders_WithDateFilter() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2025, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 7, 2, 0, 0);

        List<Order> filteredOrders = List.of(order1);
        when(orderService.getAllOrders(null, startDate, endDate)).thenReturn(filteredOrders);
        when(orderConverter.convertEntityListToDtoList(filteredOrders)).thenReturn(List.of(orderShortResponseDto1));

        mockMvc.perform(get("/v1/orders")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].orderId").value(order1.getOrderId()));

        verify(orderService).getAllOrders(null, startDate, endDate);
        verify(orderConverter).convertEntityListToDtoList(filteredOrders);
    }

    @Test
    @DisplayName("GET /v1/orders - Get all orders without filters")
    void getAllOrders_NoFilters() throws Exception {
        List<Order> allOrders = List.of(order1, order2);
        when(orderService.getAllOrders(null, null, null)).thenReturn(allOrders);
        when(orderConverter.convertEntityListToDtoList(allOrders)).thenReturn(List.of(orderShortResponseDto1, orderShortResponseDto2));

        mockMvc.perform(get("/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(orderService).getAllOrders(null, null, null);
        verify(orderConverter).convertEntityListToDtoList(allOrders);
    }

    @Test
    @DisplayName("PUT /v1/orders/{orderId} - Обновление заказа: успешный сценарий")
    void updateOrder_Success() throws Exception {

        OrderUpdateRequestDto updateRequest = OrderUpdateRequestDto.builder()
                .deliveryAddress("789 New Street")
                .contactPhone("+1122334455")
                .deliveryMethod("PICKUP")
                .build();

        Order originalOrder = order1;
        Order updatedOrder = order1.toBuilder()
                .deliveryAddress("789 New Street")
                .contactPhone("+1122334455")
                .deliveryMethod(DeliveryMethod.PICKUP)
                .build();

        OrderResponseDto expectedResponse = orderResponseDto1.toBuilder()
                .deliveryAddress("789 New Street")
                .contactPhone("+1122334455")
                .deliveryMethod("PICKUP")
                .build();

        when(orderService.getById(order1.getOrderId())).thenReturn(originalOrder);
        when(orderService.update(order1.getOrderId(), updatedOrder)).thenReturn(updatedOrder);
        when(orderConverter.convertEntityToDto(updatedOrder)).thenReturn(expectedResponse);

        mockMvc.perform(put("/v1/orders/{orderId}", order1.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deliveryAddress").value("789 New Street"))
                .andExpect(jsonPath("$.deliveryMethod").value("PICKUP"));

        verify(orderService).getById(order1.getOrderId());
        verify(orderService).update(order1.getOrderId(), updatedOrder);
        verify(orderConverter).convertEntityToDto(updatedOrder);
    }

    @Test
    @DisplayName("PUT /v1/orders/{orderId} - Обновление заказа: заказ не найден")
    void updateOrder_NotFound() throws Exception {
        Long nonExistentId = 999L;
        OrderUpdateRequestDto updateRequest = OrderUpdateRequestDto.builder()
                .deliveryAddress("789 New Street")
                .contactPhone("+1122334455")
                .deliveryMethod("PICKUP")
                .build();

        when(orderService.getById(nonExistentId))
                .thenThrow(new OrderNotFoundException("Order with id " + nonExistentId + " not found"));

        mockMvc.perform(put("/v1/orders/{orderId}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("OrderNotFoundException"));
    }

    @DisplayName("POST /v1/orders/{userId} - Неверный метод доставки")
    @Test
    void createOrder_InvalidDeliveryMethod_ValidationError() throws Exception {
        OrderCreateRequestDto invalidRequest = OrderCreateRequestDto.builder()
                .deliveryAddress("123 Main St")
                .contactPhone("+1234567890")
                .deliveryMethod("INVALID_METHOD")
                .items(List.of(
                        OrderItemCreateRequestDto.builder()
                                .productId(1L)
                                .quantity(1)
                                .build()
                ))
                .build();

        mockMvc.perform(post("/v1/orders/{userId}", user1.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.messages.deliveryMethod").value("Delivery method must be either DELIVERY or PICKUP"));
    }

    @DisplayName("POST /v1/orders/{userId} - Неверный формат телефона")
    @Test
    void createOrder_InvalidPhone_ValidationError() throws Exception {
        OrderCreateRequestDto invalidRequest = orderCreateRequestDto.toBuilder()
                .contactPhone("invalid-phone")
                .build();

        mockMvc.perform(post("/v1/orders/{userId}", user1.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"));
    }

    @DisplayName("DELETE /v1/orders/{orderId} - Отмена несуществующего заказа")
    @Test
    void cancelOrder_NotFound() throws Exception {
        Long nonExistentId = 999L;
        when(orderService.cancel(nonExistentId))
                .thenThrow(new OrderNotFoundException("Order with id " + nonExistentId + " not found"));

        mockMvc.perform(delete("/v1/orders/{orderId}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("OrderNotFoundException"));
    }
}