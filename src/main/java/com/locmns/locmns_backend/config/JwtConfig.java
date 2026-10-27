package com.locmns.locmns_backend.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Clé de signature des JWT, construite une seule fois à partir de {@code JWT_SECRET}
 * et partagée par la génération ({@code AuthService}) et la vérification ({@code JwtFilter}).
 */
@Configuration
public class JwtConfig {

    /** HS256 exige une clé d'au moins 256 bits, soit 32 octets. */
    private static final int TAILLE_MIN_OCTETS = 32;

    @Bean
    public SecretKey jwtSigningKey(@Value("${jwt.secret}") String jwtSecret) {
        byte[] octets = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (octets.length < TAILLE_MIN_OCTETS) {
            // Échec au démarrage avec un message clair plutôt qu'une erreur obscure au premier login
            throw new IllegalStateException(
                    "JWT_SECRET doit contenir au moins " + TAILLE_MIN_OCTETS + " caractères");
        }
        return Keys.hmacShaKeyFor(octets);
    }
}
