package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.EmptyOrderException;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.UserService;
import de.telran.gardenStore.service.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private Converter<Order, OrderCreateRequestDto, OrderResponseDto, OrderShortResponseDto> orderConverter;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("GET /v1/orders/history/{userId} - Get order history for user")
    void getAll() throws Exception {
        List<Order> userOrders = List.of(order1);
        List<OrderShortResponseDto> expected = List.of(orderShortResponseDto1);

        when(orderService.getAllForCurrentUser()).thenReturn(userOrders);
        when(orderConverter.convertEntityListToDtoList(userOrders)).thenReturn(expected);

        mockMvc.perform(get("/v1/orders/history"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));

        verify(orderService).getAllForCurrentUser();
        verify(orderConverter).convertEntityListToDtoList(userOrders);
    }

    @Test
    @DisplayName("GET /v1/orders/{orderId} - Get order by ID : positive case")
    void getOrderByIdPositiveCase() throws Exception {
        when(orderService.getById(order1.getOrderId())).thenReturn(order1);
        when(orderConverter.convertEntityToDto(order1)).thenReturn(orderResponseDto1);

        mockMvc.perform(get("/v1/orders/{orderId}", order1.getOrderId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(orderResponseDto1)));

        verify(orderService).getById(order1.getOrderId());
        verify(orderConverter).convertEntityToDto(order1);
    }

    @Test
    @DisplayName("GET /v1/orders/{orderId} - Get order by ID : negative case ")
    void getOrderByIdNegativeCase() throws Exception {
        Long nonExistentId = 999L;
        when(orderService.getById(nonExistentId))
                .thenThrow(new OrderNotFoundException("Order with id " + nonExistentId + " not found"));

        mockMvc.perform(get("/v1/orders/{orderId}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("OrderNotFoundException"),
                        jsonPath("$.message").value("Order with id " + nonExistentId + " not found"));

        verify(orderService).getById(nonExistentId);
    }

    @Test
    @DisplayName("POST /v1/orders/{userId} - Create new order : positive case")
    void createPositiveCase() throws Exception {
        Order orderCreated = orderToCreate.toBuilder()
                .orderId(3L)
                .build();

        when(userService.getCurrent()).thenReturn(user1);
        when(orderConverter.convertDtoToEntity(orderCreateRequestDto)).thenReturn(orderToCreate);
        when(orderService.create(orderToCreate)).thenReturn(orderCreated);
        when(orderConverter.convertEntityToDto(orderCreated)).thenReturn(orderResponseCreatedDto);

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequestDto)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(orderResponseCreatedDto)));

        verify(userService).getCurrent();
        verify(orderConverter).convertDtoToEntity(orderCreateRequestDto);
        verify(orderService).create(orderToCreate);
        verify(orderConverter).convertEntityToDto(orderCreated);
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

        List<OrderItem> orderItems = new ArrayList<>(List.of(
                OrderItem.builder()
                        .product(product3)
                        .quantity(3)
                        .build()
        ));

        Order orderInvalid = orderToCreate.toBuilder()
                .items(orderItems)
                .build();

        when(userService.getCurrent()).thenReturn(user1);
        when(orderConverter.convertDtoToEntity(orderInvalidCreateRequestDto)).thenReturn(orderInvalid);
        when(orderService.create(orderToCreate)).thenThrow(new EmptyOrderException("Order is empty."));

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderInvalidCreateRequestDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("EmptyOrderException"),
                        jsonPath("$.message").value("Order is empty."));

        verify(userService).getCurrent();
        verify(orderConverter).convertDtoToEntity(orderInvalidCreateRequestDto);
    }

    @Test
    @DisplayName("DELETE /v1/orders/{orderId} - Cancel order : positive case")
    void cancelOrderPositiveCase() throws Exception {
        Long orderId = order1.getOrderId();
        Order orderCanceled = Order.builder()
                .orderId(orderId)
                .status(OrderStatus.CANCELLED)
                .build();

        OrderResponseDto orderCanceledResponseDto = OrderResponseDto.builder()
                .orderId(orderId)
                .status("CANCELLED")
                .build();

        when(orderService.cancel(orderId)).thenReturn(orderCanceled);
        when(orderConverter.convertEntityToDto(orderCanceled)).thenReturn(orderCanceledResponseDto);

        mockMvc.perform(delete("/v1/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(orderCanceledResponseDto)));

        verify(orderService).cancel(orderId);
        verify(orderConverter).convertEntityToDto(orderCanceled);
    }

    @Test
    @DisplayName("DELETE /v1/orders/{orderId} - Cancel order : negative case")
    void cancelOrderNegativeCase() throws Exception {
        Long nonExistentId = 999L;
        when(orderService.cancel(nonExistentId))
                .thenThrow(new OrderNotFoundException("Order with id " + nonExistentId + " not found"));

        mockMvc.perform(delete("/v1/orders/{orderId}", nonExistentId))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("OrderNotFoundException"),
                        jsonPath("$.message").value("Order with id " + nonExistentId + " not found"),
                        jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }
}