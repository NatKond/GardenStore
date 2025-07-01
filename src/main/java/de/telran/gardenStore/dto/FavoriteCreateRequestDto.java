package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FavoriteCreateRequestDto {
    private Long userId;
    private Long productId;
}
