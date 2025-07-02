package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CategoryCreateRequestDto {

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 4, max = 14, message = "")
    private String name;
}