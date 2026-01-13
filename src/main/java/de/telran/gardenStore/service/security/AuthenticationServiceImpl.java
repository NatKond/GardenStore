package de.telran.gardenStore.service.security;

import de.telran.gardenStore.dto.security.LoginRequest;
import de.telran.gardenStore.dto.security.LoginResponse;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtService jwtService;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );

        AppUser user = userService.getByEmail(loginRequest.getEmail());

        return LoginResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
