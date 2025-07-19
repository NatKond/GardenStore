package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponseDto {
    private List<CartItemResponseDto> items;
}
