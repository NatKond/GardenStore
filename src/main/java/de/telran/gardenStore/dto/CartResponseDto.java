package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponseDto {

    Long cartId;

    Long userId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CartItemResponseDto> items;
}
