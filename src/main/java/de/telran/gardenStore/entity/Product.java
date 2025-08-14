package de.telran.gardenStore.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private BigDecimal discountPrice;

    private BigDecimal price;

    @ToString.Exclude
    private String description;

    @ToString.Exclude
    private String imageUrl;

    @ToString.Exclude
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ToString.Exclude
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}

