package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {

    @EqualsAndHashCode.Include
    private Long categoryId;

    private String name;

    List<ProductShortResponseDto> products;
}
