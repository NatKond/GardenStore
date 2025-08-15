package de.telran.gardenStore.entity;

import de.telran.gardenStore.enums.Role;
import de.telran.gardenStore.serializer.SensitiveData;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long userId;

    private String name;

    private String email;

    @SensitiveData(visibleChars = 4)
    private String phoneNumber;

    @SensitiveData
    private String passwordHash;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>(List.of(Role.ROLE_USER));

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

}
