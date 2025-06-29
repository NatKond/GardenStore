package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateRequestDto {
    @NotBlank(message = "Category name cannot be blank")
    private String name;
}