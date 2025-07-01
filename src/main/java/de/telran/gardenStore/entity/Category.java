package de.telran.gardenStore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long categoryId;

    private String name;

}