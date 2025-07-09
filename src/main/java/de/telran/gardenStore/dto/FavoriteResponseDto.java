package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteResponseDto {

    @EqualsAndHashCode.Include
    private Long favoriteId;

    @JsonIgnore
    private Long userId;

    private ProductShortResponseDto product;
    //private Long productId;

}
