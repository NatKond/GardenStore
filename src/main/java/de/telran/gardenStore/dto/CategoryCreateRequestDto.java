package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder(toBuilder = true)
public class CategoryCreateRequestDto {

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 4, max = 30, message = "Category name should be from 2 to 30 characters")
    private String name;
}