package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    private Long orderId;

    private String status;

    private String deliveryAddress;

    private String contactPhone;

    private String deliveryMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<OrderItemResponseDto> items;
}
