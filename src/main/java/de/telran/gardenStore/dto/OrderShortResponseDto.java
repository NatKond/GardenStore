package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.telran.gardenStore.serializer.SensitiveData;
import lombok.*;

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

    @SensitiveData(visibleChars = 4)
    private String contactPhone;

    private String deliveryMethod;
}
