package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class ProductCreateRequestDto {

    @NotBlank(message = "Name should not be blank")
    @Size(min = 1, max = 100, message = "Product name should be from 1 to 30 characters")
    private String name;

    @NotBlank(message = "Product description should not be blank")
    @Size(min = 4, max = 255, message = "Product description should be from 4 to 100 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Positive(message = "Discount price must be positive")
    private BigDecimal discountPrice;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @URL(message = "Image URL must be valid")
    private String imageUrl;
}
