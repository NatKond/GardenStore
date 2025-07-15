package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteResponseDto {

    @EqualsAndHashCode.Include
    private Long favoriteId;

    private ProductShortResponseDto product;
    //private Long productId;
}
