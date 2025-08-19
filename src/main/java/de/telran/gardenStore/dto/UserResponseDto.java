package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    @EqualsAndHashCode.Include
    private Long userId;

    private String name;

    private String email;

    private String phone;

    private List<String> roles;

    private List<FavoriteResponseDto> favorites;
}
