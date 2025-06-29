package de.telran.gardenStore.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    private Long id;
    private String name;

    public static CategoryResponseDtoBuilder builder() {
        return new CategoryResponseDtoBuilder();
    }
}
