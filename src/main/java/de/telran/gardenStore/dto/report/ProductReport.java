package de.telran.gardenStore.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.telran.gardenStore.dto.ProductShortResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductReport {

    private ProductShortResponseDto product;

    private Long quantity;
}
