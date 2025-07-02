package de.telran.gardenStore.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class UserResponseDto {

    @EqualsAndHashCode.Include
    private Long userId;

    private String name;

    private String email;
}
