package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
@ToString
public class CategoryResponseDto {

    private Long categoryId;

    private String name;
}
