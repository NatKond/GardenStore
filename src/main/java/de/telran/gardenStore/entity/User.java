package de.telran.gardenStore.entity;
import de.telran.gardenStore.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_users")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long userId;

    private String name;

    private String email;

    private String phoneNumber;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
