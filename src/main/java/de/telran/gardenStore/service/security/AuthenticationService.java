package de.telran.gardenStore.service.security;

import de.telran.dto.security.LoginRequest;
import de.telran.dto.security.LoginResponse;

public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest loginRequest);
}
