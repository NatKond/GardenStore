package de.telran.gardenStore.controller;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.OrderConverter;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class PaymentControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderConverter orderConverter;

    @Test
    @DisplayName("POST /v1/payment - Successful payment")
    void processPaymentPositiveCase() throws Exception {
        Long orderId = order1.getOrderId();
        BigDecimal paymentAmount = orderResponseDto1.getTotalAmount();
        OrderResponseDto orderPaidResponseDto = orderResponseDto1.toBuilder()
                .status(OrderStatus.PAID.name())
                .build();

        Order paidOrder = order1.toBuilder()
                .status(OrderStatus.PAID)
                .build();

        when(orderService.getTotalAmount(orderId)).thenReturn(paymentAmount);
        when(orderService.getById(orderId)).thenReturn(order1);
        when(orderService.updateStatus(orderId, OrderStatus.PAID)).thenReturn(paidOrder);
        when(orderConverter.convertEntityToDto(paidOrder)).thenReturn(orderPaidResponseDto);

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", paymentAmount.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.orderId").value(orderId),
                        jsonPath("$.status").value("PAID"));
    }

    @Test
    @DisplayName("POST /v1/payment - Incorrect payment amount")
    void processPaymentNegativeCase() throws Exception {
        Long orderId = order1.getOrderId();
        BigDecimal paymentAmount = orderResponseDto1.getTotalAmount();
        BigDecimal incorrectAmount = new BigDecimal("20.00");

        when(orderService.getTotalAmount(orderId)).thenReturn(paymentAmount);
        when(orderService.getById(orderId)).thenReturn(order1);

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", incorrectAmount.toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.exception").value("IncorrectPaymentAmountException"),
                        jsonPath("$.message").value("Payment amount is incorrect"));
    }

    @Test
    @DisplayName("POST /v1/payment - Order not in AWAITING_PAYMENT status")
    void processPayment_WrongStatus() throws Exception {
        Long orderId = order2.getOrderId();
        BigDecimal paymentAmount = product3.getDiscountPrice();

        when(orderService.getTotalAmount(orderId)).thenReturn(paymentAmount);
        when(orderService.getById(orderId)).thenReturn(order2);

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", paymentAmount.toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.exception").value("OrderPaymentRejectedException"),
                        jsonPath("$.message").value("Order cannot be paid in current status: CREATED"));
    }
}