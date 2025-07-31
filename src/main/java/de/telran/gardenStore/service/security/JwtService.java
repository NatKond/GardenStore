package de.telran.gardenStore.service.security;

import de.telran.gardenStore.entity.AppUser;

public interface JwtService {

    String generateToken(AppUser user);

    String extractUsername(String token);
}
