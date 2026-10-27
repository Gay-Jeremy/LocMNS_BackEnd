package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.config.JwtConfig;
import com.locmns.locmns_backend.dto.request.LoginRequestDto;
import com.locmns.locmns_backend.model.CategorieUtilisateur;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.JwtFilter;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.AuthService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Vérifie la chaîne JWT de bout en bout : clé de signature, génération du jeton
 * à la connexion ({@link AuthService}) et vérification à chaque requête ({@link JwtFilter}).
 */
class JwtUnitTest {

    private static final String SECRET = "une-cle-de-test-suffisamment-longue-pour-hs256";

    private SecretKey cle;
    private UtilisateurDetails details;

    @BeforeEach
    void init() {
        cle = new JwtConfig().jwtSigningKey(SECRET);

        CategorieUtilisateur categorie = new CategorieUtilisateur();
        categorie.setLibelle("Étudiant");
        categorie.setCode("USER");
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(3);
        utilisateur.setEmail("lucas.bernard@locmns.fr");
        utilisateur.setCategorieUtilisateur(categorie);
        details = new UtilisateurDetails(utilisateur);
    }

    @AfterEach
    void nettoyer() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Un JWT_SECRET trop court est refusé dès le démarrage")
    void secretTropCourt() {
        assertThatThrownBy(() -> new JwtConfig().jwtSigningKey("trop-court"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("32");
    }

    @Test
    @DisplayName("Login valide : le jeton émis est accepté par le filtre et authentifie l'utilisateur")
    void loginPuisRequeteAuthentifiee() throws Exception {
        // Connexion
        AuthenticationProvider provider = mock(AuthenticationProvider.class);
        when(provider.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));
        AuthService authService = new AuthService(cle, provider);

        ResponseEntity<String> reponse = authService.login(
                new LoginRequestDto("lucas.bernard@locmns.fr", "motdepasse"));

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String jwt = reponse.getBody();

        // Requête suivante avec le jeton
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        when(userDetailsService.loadUserByUsername("lucas.bernard@locmns.fr")).thenReturn(details);
        JwtFilter filtre = new JwtFilter(cle, userDetailsService);

        MockHttpServletRequest requete = new MockHttpServletRequest();
        requete.addHeader("Authorization", "Bearer " + jwt);
        FilterChain chaine = mock(FilterChain.class);

        filtre.doFilter(requete, new MockHttpServletResponse(), chaine);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                .isEqualTo("lucas.bernard@locmns.fr");
        verify(chaine).doFilter(any(), any());
    }

    @Test
    @DisplayName("Mauvais identifiants : 401 et aucun jeton")
    void loginInvalide() {
        AuthenticationProvider provider = mock(AuthenticationProvider.class);
        when(provider.authenticate(any())).thenThrow(new BadCredentialsException("refusé"));

        ResponseEntity<String> reponse = new AuthService(cle, provider)
                .login(new LoginRequestDto("lucas.bernard@locmns.fr", "faux"));

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(reponse.getBody()).isNull();
    }

    @Test
    @DisplayName("Jeton signé avec une autre clé : rejeté, la requête continue sans authentification")
    void jetonFalsifie() throws Exception {
        SecretKey autreCle = new JwtConfig().jwtSigningKey("une-autre-cle-tout-aussi-longue-pour-hs256");
        String jwtFalsifie = Jwts.builder()
                .subject("lucas.bernard@locmns.fr")
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(autreCle)
                .compact();

        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtFilter filtre = new JwtFilter(cle, userDetailsService);
        MockHttpServletRequest requete = new MockHttpServletRequest();
        requete.addHeader("Authorization", "Bearer " + jwtFalsifie);
        FilterChain chaine = mock(FilterChain.class);

        filtre.doFilter(requete, new MockHttpServletResponse(), chaine);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(chaine).doFilter(any(), any());
    }

    @Test
    @DisplayName("Jeton expiré : rejeté")
    void jetonExpire() throws Exception {
        String jwtExpire = Jwts.builder()
                .subject("lucas.bernard@locmns.fr")
                .expiration(new Date(System.currentTimeMillis() - 60_000))
                .signWith(cle)
                .compact();

        JwtFilter filtre = new JwtFilter(cle, mock(UserDetailsService.class));
        MockHttpServletRequest requete = new MockHttpServletRequest();
        requete.addHeader("Authorization", "Bearer " + jwtExpire);

        filtre.doFilter(requete, new MockHttpServletResponse(), mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
