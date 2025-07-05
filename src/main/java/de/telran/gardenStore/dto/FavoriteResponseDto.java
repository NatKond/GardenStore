package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteResponseDto {

    @EqualsAndHashCode.Include
    private Long favoriteId;

    private Long userId;

    private Long productId;

}
