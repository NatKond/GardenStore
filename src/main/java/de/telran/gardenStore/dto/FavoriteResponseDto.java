package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
public class FavoriteResponseDto {

    @EqualsAndHashCode.Include
    private Long favoriteId;

    private Long userId;

    private Long productId;

}
