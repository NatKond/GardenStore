package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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

    private String contactPhone;

    private String deliveryMethod;
}
