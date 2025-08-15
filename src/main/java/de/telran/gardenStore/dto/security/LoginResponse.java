package de.telran.gardenStore.dto.security;

import de.telran.gardenStore.serializer.SensitiveData;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class LoginResponse {

    @SensitiveData
    private String token;
}
