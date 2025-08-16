package de.telran.gardenStore.dto.security;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class LoginResponse {

    private String token;
}
