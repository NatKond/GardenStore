package de.telran.gardenStore.controller;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.converter.OrderConverter;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.exception.OrderNotFoundException;
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
    void processPayment_Success() throws Exception {
        Long orderId = 1L;
        BigDecimal paymentAmount = new BigDecimal("28.47");
        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(orderId)
                .status(OrderStatus.PAID.name())
                .build();

        Order paidOrder = order1.toBuilder().status(OrderStatus.PAID).build();

        when(orderService.getTotalAmount(orderId)).thenReturn(paymentAmount);
        when(orderService.getById(orderId)).thenReturn(order1);
        when(orderService.updateStatus(orderId, OrderStatus.PAID)).thenReturn(paidOrder);
        when(orderConverter.convertEntityToDto(paidOrder)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", paymentAmount.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @DisplayName("POST /v1/payment - Incorrect payment amount")
    void processPayment_IncorrectAmount() throws Exception {
        Long orderId = 1L;
        BigDecimal paymentAmount = new BigDecimal("20.00");
        BigDecimal totalAmount = new BigDecimal("28.47");

        when(orderService.getTotalAmount(orderId)).thenReturn(totalAmount);
        when(orderService.getById(orderId)).thenReturn(order1);

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", paymentAmount.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("IncorrectPaymentAmountException"))
                .andExpect(jsonPath("$.message").value("Payment amount is incorrect"));
    }

    @Test
    @DisplayName("POST /v1/payment - Order not in AWAITING_PAYMENT status")
    void processPayment_WrongStatus() throws Exception {
        Long orderId = 2L;
        BigDecimal paymentAmount = new BigDecimal("5.75");

        when(orderService.getTotalAmount(orderId)).thenReturn(paymentAmount);
        when(orderService.getById(orderId)).thenReturn(order2);

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", paymentAmount.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("OrderPaymentRejectedException"))
                .andExpect(jsonPath("$.message").value("Order cannot be paid in current status: CREATED"));
    }

    @Test
    @DisplayName("POST /v1/payment - Negative orderId")
    void processPayment_NegativeOrderId() throws Exception {
        mockMvc.perform(post("/v1/payment")
                        .param("orderId", "-1")
                        .param("paymentAmount", "10.00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /v1/payment - Negative paymentAmount")
    void processPayment_NegativePaymentAmount() throws Exception {
        mockMvc.perform(post("/v1/payment")
                        .param("orderId", "1")
                        .param("paymentAmount", "-10.00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /v1/payment - Missing orderId")
    void processPayment_MissingOrderId() throws Exception {
        mockMvc.perform(post("/v1/payment")
                        .param("paymentAmount", "10.00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /v1/payment - Missing paymentAmount")
    void processPayment_MissingPaymentAmount() throws Exception {
        mockMvc.perform(post("/v1/payment")
                        .param("orderId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /v1/payment - Order not found")
    void processPayment_OrderNotFound() throws Exception {
        Long orderId = 999L;
        BigDecimal paymentAmount = new BigDecimal("10.00");

        when(orderService.getById(orderId)).thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", paymentAmount.toString()))
                .andExpect(status().isNotFound());
    }
}