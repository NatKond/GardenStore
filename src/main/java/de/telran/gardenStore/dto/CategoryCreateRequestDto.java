package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class CategoryCreateRequestDto {

    @NotBlank(message = "Category name cannot be blank")
    private String name;
}