package de.telran.gardenStore.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
@AllArgsConstructor

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long  productId ;

    private String name;

    private BigDecimal discountPrice;

    private BigDecimal price ;

    private Long categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;

    private String imageUrl;



}
