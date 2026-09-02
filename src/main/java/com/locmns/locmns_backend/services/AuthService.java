package com.locmns.locmns_backend.services;


import com.locmns.locmns_backend.dto.request.LoginRequestDto;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    @Value("${jwt.secret}")
    protected String jwtSecret;

    private final AuthenticationProvider authenticationProvider;

    @Override
    public ResponseEntity<String> creerJwt(LoginRequestDto request) {

        try {
            UtilisateurDetails utilisateurDetails = (UtilisateurDetails) authenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    ))
                    .getPrincipal();

            String jwt = Jwts.builder()
                    .setSubject(request.email())
                    .addClaims(Map.of(
                            "id", utilisateurDetails.getUtilisateur().getId(),
                            "role", utilisateurDetails
                                    .getUtilisateur()
                                    .getCategorieUtilisateur()
                                    .getLibelle()
                    ))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86_400_000)) // 24 h
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();

            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (AuthenticationException e) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

