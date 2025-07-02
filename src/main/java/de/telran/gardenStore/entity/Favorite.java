package de.telran.gardenStore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorites")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long favoriteId;

    private Long userId;

    private Long productId;
}
