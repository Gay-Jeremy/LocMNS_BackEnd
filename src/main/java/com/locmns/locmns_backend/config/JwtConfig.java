package com.locmns.locmns_backend.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Clé de signature des JWT, construite une seule fois et partagée par la génération
 * ({@code AuthService}) et la vérification ({@code JwtFilter}).
 *
 * <ul>
 *   <li>{@code JWT_SECRET} renseigné : la clé est dérivée de cette valeur (32 caractères minimum).</li>
 *   <li>{@code JWT_SECRET} vide ou absent : une clé aléatoire de 256 bits est générée au démarrage
 *       (générateur cryptographique {@link java.security.SecureRandom}). Les jetons émis deviennent
 *       invalides à chaque redémarrage : les utilisateurs doivent se reconnecter.</li>
 * </ul>
 */
@Configuration
public class JwtConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    /** HS256 exige une clé d'au moins 256 bits, soit 32 octets. */
    private static final int TAILLE_MIN_OCTETS = 32;

    @Bean
    public SecretKey jwtSigningKey(@Value("${jwt.secret:}") String jwtSecret) {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            logger.warn("JWT_SECRET non défini : génération d'une clé aléatoire temporaire. "
                    + "Les sessions seront perdues à chaque redémarrage de l'application.");
            return Jwts.SIG.HS256.key().build();
        }

        byte[] octets = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (octets.length < TAILLE_MIN_OCTETS) {
            // Échec au démarrage avec un message clair plutôt qu'une erreur obscure au premier login
            throw new IllegalStateException(
                    "JWT_SECRET doit contenir au moins " + TAILLE_MIN_OCTETS + " caractères");
        }
        return Keys.hmacShaKeyFor(octets);
    }
}
