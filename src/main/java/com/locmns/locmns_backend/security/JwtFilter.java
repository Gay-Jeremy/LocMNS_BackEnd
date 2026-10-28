package com.locmns.locmns_backend.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(JwtFilter.class);

    private final SecretKey jwtSigningKey;

    protected final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.toLowerCase().startsWith("bearer ")) {
            String jwt = token.substring(7);

            try {
                String email = Jwts.parser()
                        .verifyWith(jwtSigningKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload()
                        .getSubject();

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            } catch (JwtException | UsernameNotFoundException | IllegalArgumentException e) {
                // Jeton invalide ou expiré : la requête continue sans authentification
                // (Spring Security renverra 401 si la ressource est protégée)
                logger.debug("JWT rejeté : {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);

    }
}
