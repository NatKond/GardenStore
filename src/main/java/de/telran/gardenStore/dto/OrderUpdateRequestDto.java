package de.telran.gardenStore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderUpdateRequestDto {
    private String deliveryAddress;
    private String contactPhone;
    private String deliveryMethod;
}