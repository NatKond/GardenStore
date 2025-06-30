package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CategoryCreateRequestDto {

    @NotBlank(message = "Category name cannot be blank")
    private String name;
}