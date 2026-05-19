package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.UtilisateurService;
import com.locmns.locmns_backend.view.UtilisateurView;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UtilisateurService utilisateurService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping("/sign-in")
    @JsonView( UtilisateurView.class )
    public ResponseEntity<Utilisateur>  signIn(
            @RequestBody @Validated(Utilisateur.OnCreate.class) Utilisateur utilisateurToInsert) {

        utilisateurService.Insert(utilisateurToInsert);

        return new ResponseEntity<>(utilisateurToInsert,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @JsonView( UtilisateurView.class )
    public ResponseEntity<String>  login (
            @RequestBody Utilisateur utilisateur) {

        try {
            UtilisateurDetails utilisateurDetails = (UtilisateurDetails) authenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            utilisateur.getEmail(),
                            utilisateur.getPassword()
                    ))
                    .getPrincipal();

        String jwt = Jwts.builder()
                    .setSubject(utilisateur.getEmail())
                    .addClaims(Map.of("role", utilisateurDetails.getUtilisateur().getCategorieUtilisateur().getLibelle()))
                    .signWith(SignatureAlgorithm.HS256, "root")
                    .compact();

        return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (AuthenticationException e) {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
