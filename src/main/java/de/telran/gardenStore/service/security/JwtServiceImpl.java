package de.telran.gardenStore.service.security;

import de.telran.gardenStore.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;

    private final Integer expirationTime;

    public JwtServiceImpl(@Value("${jwt.token.sign.secret.key}") String jwtSingKey,
                          @Value("${jwt.token.expiration.minutes}") Integer expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSingKey));
        this.expirationTime = expirationTime;
    }

    @Override
    public String generateToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>(Map.of(
                "userId", user.getUserId(),
                "name", user.getName(),
                "roles", user.getRoles()
        ));

        return generateToken(claims, user);
    }

    private String generateToken(Map<String, Object> claims, AppUser user) {
        return Jwts.builder()
                .claims()
                .subject(user.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(expirationTime))))
                .add(claims)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = getClaimsFromToken(token);
        log.info("claims {}", claims);
        return claims.getSubject();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
