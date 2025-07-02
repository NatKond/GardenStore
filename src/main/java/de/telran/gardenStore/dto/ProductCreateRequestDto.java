package de.telran.gardenStore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.URL;


@Data
public class ProductCreateRequestDto {

    @NotBlank(message = "Name must not be blank")
    private String name;

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
