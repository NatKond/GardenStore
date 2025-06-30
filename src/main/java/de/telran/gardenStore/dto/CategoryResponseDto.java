package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
public class CategoryResponseDto {

    @EqualsAndHashCode.Include
    private Long categoryId;

    private String name;
}
