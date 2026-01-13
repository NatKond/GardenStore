package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {

    private Long categoryId;

    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    List<ProductShortResponseDto> products;
}
