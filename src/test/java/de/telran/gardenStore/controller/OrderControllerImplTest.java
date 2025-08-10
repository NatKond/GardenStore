package de.telran.gardenStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.Converter;
import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.EmptyOrderException;
import de.telran.gardenStore.exception.OrderNotFoundException;
import de.telran.gardenStore.service.OrderService;
import de.telran.gardenStore.service.ProductService;
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
import java.util.stream.Collectors;

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
    private Converter<Product, ProductCreateRequestDto, ProductResponseDto, ProductShortResponseDto> productConverter;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("GET /v1/orders/history - Get order history for current user")
    void getAllForCurrentUser() throws Exception {
        List<Order> userOrders = List.of(order1, order3);
        List<OrderShortResponseDto> expected = List.of(orderShortResponseDto1, orderShortResponseDto3);

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
    @DisplayName("GET /v1/orders/history/delivered- Get all delivered orders history for current user")
    void getAllDeliveredForCurrentUser() throws Exception {
        List<Order> userOrders = List.of(order3);
        List<OrderResponseDto> expected = List.of(orderResponseDto3);

        when(orderService.getAllDeliveredForCurrentUser()).thenReturn(userOrders);
        when(orderConverter.convertEntityToDto(order3)).thenReturn(orderResponseDto3);

        mockMvc.perform(get("/v1/orders/history/delivered"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));

        verify(orderService).getAllDeliveredForCurrentUser();
        verify(orderConverter).convertEntityToDto(order3);
    }

    @Test
    @DisplayName("GET /v1/orders/history/{userId} - Get order history for user")
    void getAll() throws Exception {
        List<Order> userOrders = List.of(order1, order2, order3, order4);
        List<OrderShortResponseDto> expected = List.of(orderShortResponseDto1, orderShortResponseDto2, orderShortResponseDto3, orderShortResponseDto4);

        when(orderService.getAll()).thenReturn(userOrders);
        when(orderConverter.convertEntityListToDtoList(userOrders)).thenReturn(expected);

        mockMvc.perform(get("/v1/orders"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));

        verify(orderService).getAll();
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

        when(orderConverter.convertDtoToEntity(orderCreateRequestDto)).thenReturn(orderToCreate);
        when(orderService.create(
                orderToCreate.getDeliveryAddress(),
                orderToCreate.getDeliveryMethod(),
                orderToCreate.getContactPhone(),
                orderToCreate.getItems().stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getProductId(), OrderItem::getQuantity))
        )).thenReturn(orderCreated);
        when(orderConverter.convertEntityToDto(orderCreated)).thenReturn(orderResponseCreatedDto);

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequestDto)))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(orderResponseCreatedDto)));

        verify(orderService).create(
                orderToCreate.getDeliveryAddress(),
                orderToCreate.getDeliveryMethod(),
                orderToCreate.getContactPhone(),
                orderToCreate.getItems().stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getProductId(), OrderItem::getQuantity)));
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

        when(orderService.create(
                orderInvalid.getDeliveryAddress(),
                orderInvalid.getDeliveryMethod(),
                orderInvalid.getContactPhone(),
                orderInvalid.getItems().stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getProductId(), OrderItem::getQuantity))))
                .thenThrow(new EmptyOrderException("Order is empty."));

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderInvalidCreateRequestDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.exception").value("EmptyOrderException"),
                        jsonPath("$.message").value("Order is empty."));

    }

    @Test
    @DisplayName("POST /v1/orders/items?orderId={orderId}&productId={productId}&quantity={quantity} - Update order item")
    void addItem() throws Exception {
        Order order = order1.toBuilder()
                .status(OrderStatus.CREATED)
                .build();
        Integer quantity = 2;

        List<OrderItem> orderItemsUpdated = new ArrayList<>(order.getItems());
        orderItemsUpdated.add(OrderItem.builder()
                .product(product3)
                .quantity(quantity)
                .build());
        Order orderUpdated = order.toBuilder()
                .items(orderItemsUpdated)
                .build();

        List<OrderItemResponseDto> orderItemResponseDto = new ArrayList<>(orderResponseDto1.getItems());
        orderItemResponseDto.add(OrderItemResponseDto.builder()
                .orderItemId(4L)
                .product(productShortResponseDto3)
                .quantity(quantity)
                .priceAtPurchase(product3.getDiscountPrice())
                .build());
        OrderResponseDto expected = orderResponseDto1.toBuilder()
                .status(order.getStatus().toString())
                .items(orderItemResponseDto)
                .build();

        when(orderService.addItem(order.getOrderId(), product3.getProductId(), quantity)).thenReturn(orderUpdated);
        when(orderConverter.convertEntityToDto(orderUpdated)).thenReturn(expected);

        mockMvc.perform(post("/v1/orders/items?orderId={orderId}&productId={productId}&quantity={quantity}",
                        order.getOrderId(),
                        productShortResponseDto3.getProductId(),
                        quantity)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));

        verify(orderService).addItem(order.getOrderId(), product3.getProductId(), quantity);
        verify(orderConverter).convertEntityToDto(orderUpdated);
    }

    @Test
    @DisplayName("PUT /v1/orders/items?orderItemId={orderItemId}&quantity={quantity} - Update order item")
    void updateItem() throws Exception {
        Order order = order1.toBuilder()
                .status(OrderStatus.CREATED)
                .build();

        Integer quantity = 3;
        Long orderItemId = orderItem1.getOrderItemId();

        List<OrderItem> orderItemsUpdated = new ArrayList<>(order.getItems());
        orderItemsUpdated.remove(orderItem1);
        orderItemsUpdated.add(orderItem1.toBuilder()
                .quantity(quantity)
                .build());
        Order orderUpdated = order.toBuilder()
                .items(orderItemsUpdated)
                .build();

        List<OrderItemResponseDto> orderItemResponseDto = new ArrayList<>(orderResponseDto1.getItems());
        orderItemResponseDto.remove(orderItemResponseDto1);
        orderItemResponseDto.add(orderItemResponseDto1.toBuilder()
                .quantity(quantity)
                .build());
        OrderResponseDto expected = orderResponseDto1.toBuilder()
                .status(order.getStatus().toString())
                .items(orderItemResponseDto)
                .build();

        when(orderService.updateItem(orderItemId, quantity)).thenReturn(orderUpdated);
        when(orderConverter.convertEntityToDto(orderUpdated)).thenReturn(expected);

        mockMvc.perform(put("/v1/orders/items?orderItemId={orderItemId}&quantity={quantity}",
                        orderItemId,
                        quantity)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isAccepted(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));

        verify(orderService).updateItem(orderItemId,quantity);
        verify(orderConverter).convertEntityToDto(orderUpdated);
    }

    @Test
    @DisplayName("DELETE /v1/orders/items/{orderItemId} - Delete order item")
    void deleteItem() throws Exception {
        Order order = order1.toBuilder()
                .status(OrderStatus.CREATED)
                .build();

        Long orderItemId = orderItem1.getOrderItemId();

        List<OrderItem> orderItemsUpdated = new ArrayList<>(order.getItems());
        orderItemsUpdated.remove(orderItem1);
        Order orderUpdated = order.toBuilder()
                .items(orderItemsUpdated)
                .build();

        List<OrderItemResponseDto> orderItemResponseDto = new ArrayList<>(orderResponseDto1.getItems());
        orderItemResponseDto.remove(orderItemResponseDto1);
        OrderResponseDto expected = orderResponseDto1.toBuilder()
                .status(order.getStatus().toString())
                .items(orderItemResponseDto)
                .build();

        when(orderService.removeItem(orderItemId)).thenReturn(orderUpdated);
        when(orderConverter.convertEntityToDto(orderUpdated)).thenReturn(expected);

        mockMvc.perform(delete("/v1/orders/items/{orderItemId} ", orderItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(objectMapper.writeValueAsString(expected)));

        verify(orderService).removeItem(orderItemId);
        verify(orderConverter).convertEntityToDto(orderUpdated);
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