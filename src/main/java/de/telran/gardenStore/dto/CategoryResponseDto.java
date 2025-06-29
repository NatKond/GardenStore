package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class CategoryResponseDto {
    private Long id;
    private String name;

    public static CategoryResponseDtoBuilder builder() {
        return new CategoryResponseDtoBuilder();
    }
}
