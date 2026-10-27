package com.locmns.locmns_backend.services;


import com.locmns.locmns_backend.dto.request.LoginRequestDto;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final SecretKey jwtSigningKey;

    private final AuthenticationProvider authenticationProvider;

    @Override
    public ResponseEntity<String> login(LoginRequestDto request) {

        try {
            UtilisateurDetails utilisateurDetails = (UtilisateurDetails) authenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    ))
                    .getPrincipal();

            String jwt = Jwts.builder()
                    .subject(request.email())
                    .claims(Map.of(
                            "id", utilisateurDetails.getUtilisateur().getId(),
                            "role", utilisateurDetails
                                    .getUtilisateur()
                                    .getCategorieUtilisateur()
                                    .getLibelle()
                    ))
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 86_400_000)) // 24 h
                    .signWith(jwtSigningKey)
                    .compact();

            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (AuthenticationException e) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

