package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderShortResponseDto {

    private Long orderId;

    private Long userId;

    private String status;

    private String deliveryAddress;

    private BigDecimal totalAmount;

    private String contactPhone;

    private String deliveryMethod;
}
