package de.telran.gardenStore.dto;

import lombok.*;

@Data
public class FavoriteCreateRequestDto {

    private Long userId;

    private Long productId;
}
