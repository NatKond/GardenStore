package de.telran.gardenStore.service.security;

import de.telran.gardenStore.dto.security.LoginRequest;
import de.telran.gardenStore.dto.security.LoginResponse;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest loginRequest);
}
