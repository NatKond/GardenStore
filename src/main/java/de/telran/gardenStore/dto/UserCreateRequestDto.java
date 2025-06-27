package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class UserCreateRequestDto {

    private String name;

    private String email;

    private String phoneNumber;

    private String passwordHash;
}
