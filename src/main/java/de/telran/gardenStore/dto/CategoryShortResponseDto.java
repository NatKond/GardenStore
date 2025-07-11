package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryShortResponseDto {
    @EqualsAndHashCode.Include
    private Long categoryId;

    private String name;
}
