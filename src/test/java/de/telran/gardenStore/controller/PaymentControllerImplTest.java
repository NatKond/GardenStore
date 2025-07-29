package de.telran.gardenStore.controller;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.dto.OrderResponseDto;
import de.telran.gardenStore.service.PaymentService;
import de.telran.gardenStore.service.security.JwtAuthFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class PaymentControllerImplTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;
    @MockitoBean
    private PaymentService paymentService;

    @Test
    @DisplayName("POST /v1/payment - Delegates to PaymentService")
    void processPayment_DelegatesToService() throws Exception {
        Long orderId = order1.getOrderId();
        BigDecimal paymentAmount = orderResponseDto1.getTotalAmount();
        OrderResponseDto expected = orderResponseDto1.toBuilder()
                .status("PAID")
                .build();

        when(paymentService.processPayment(orderId, paymentAmount)).thenReturn(expected);

        mockMvc.perform(post("/v1/payment")
                        .param("orderId", orderId.toString())
                        .param("paymentAmount", paymentAmount.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        verify(paymentService).processPayment(orderId, paymentAmount);
    }
}