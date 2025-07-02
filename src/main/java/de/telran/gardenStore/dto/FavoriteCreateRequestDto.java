package de.telran.gardenStore.dto;

import lombok.*;

@Data
@Builder
public class FavoriteCreateRequestDto {

    private Long userId;

    private Long productId;
}
