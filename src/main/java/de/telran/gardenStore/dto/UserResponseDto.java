package de.telran.gardenStore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class UserResponseDto {

    private Long id;

    private String name;
}
