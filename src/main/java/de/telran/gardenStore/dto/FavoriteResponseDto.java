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
    private Long userId;
    private Long productId;
    private Long favoriteId;
}
