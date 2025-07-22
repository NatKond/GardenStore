package de.telran.gardenStore.dto;

import de.telran.gardenStore.enums.DeliveryMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class OrderCreateRequestDto {

    @NotBlank(message = "Delivery address cannot be blank")
    private String deliveryAddress;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$")
    private String contactPhone;

    //    @NotBlank(message = "Delivery method cannot be blank")
//    private String deliveryMethod;
    @NotNull
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;


    @NotEmpty
    private List<OrderItemCreateRequestDto> items;
}