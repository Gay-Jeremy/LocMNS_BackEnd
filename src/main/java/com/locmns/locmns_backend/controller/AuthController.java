package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.LoginRequestDto;
import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.validation.ValidationGroups;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.IUtilisateurService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.secret}")
    protected String jwtSecret;

    private final IUtilisateurService utilisateurService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping("/api/sign-in")
    public ResponseEntity<UtilisateurRequestDto>  signIn(
            @RequestBody @Validated(ValidationGroups.OnCreate.class) UtilisateurRequestDto utilisateurToInsert) {

        utilisateurService.creerUtilisateur(utilisateurToInsert);

        return new ResponseEntity<>(utilisateurToInsert,HttpStatus.CREATED);
    }


    @PostMapping("/api/login")
    public ResponseEntity<String>  login (
            @RequestBody LoginRequestDto request) {

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
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();

        return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (AuthenticationException e) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
