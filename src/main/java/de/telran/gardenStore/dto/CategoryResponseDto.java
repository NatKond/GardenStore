package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class CategoryResponseDto {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;

}
