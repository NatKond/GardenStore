package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.telran.gardenStore.serializer.SensitiveData;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    private Long orderId;

    private Long userId;

    private String status;

    @SensitiveData
    private String deliveryAddress;

    @SensitiveData(visibleChars = 4)
    private String contactPhone;

    private String deliveryMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderItemResponseDto> items;

    private BigDecimal totalAmount;
}
