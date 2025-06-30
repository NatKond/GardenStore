package de.telran.gardenStore.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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

    private Double discountPrice;

    private Double price ;

    private Long categoryId;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String description;

    private String imageUrl;



}
