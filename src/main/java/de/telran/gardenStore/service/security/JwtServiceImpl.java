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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {


    private final SecretKey secretKey;

    public JwtServiceImpl(@Value("${jwttoken.sing.secret.key}") String jwtSingKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSingKey));
    }

    @Override
    public String generateToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>(Map.of(
                "userId", user.getUserId(),
                "name", user.getName())
        );

        return generateToken(claims, user);
    }

    private String generateToken(Map<String, Object> claims, AppUser user) {
        return Jwts.builder()
                .claims()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Duration.ofMinutes(5).toMillis()))
                .subject(user.getEmail())
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
