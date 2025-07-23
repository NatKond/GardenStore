package de.telran.gardenStore.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorites")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

