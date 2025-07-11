package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    @EqualsAndHashCode.Include
    private Long userId;

    private String name;

    private String email;

    private String phoneNumber;

    private String role;

    private List<FavoriteResponseDto> favorites;
}
