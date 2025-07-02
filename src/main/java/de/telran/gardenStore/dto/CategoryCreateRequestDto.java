package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class CategoryCreateRequestDto {

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 4, max = 14, message = "Category name should be from 2 to 30 characters")
    private String name;
}