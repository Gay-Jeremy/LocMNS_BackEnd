package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.LoginRequestDto;
import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.dto.validation.ValidationGroups;
import com.locmns.locmns_backend.services.IAuthService;
import com.locmns.locmns_backend.services.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final IUtilisateurService utilisateurService;
    private final IAuthService authService;

    @PostMapping("/api/sign-in")
    public ResponseEntity<UtilisateurResponseDto> signIn(
            @RequestBody @Validated(ValidationGroups.OnCreate.class) UtilisateurRequestDto utilisateurToInsert) {

        // On renvoie le DTO de réponse (sans mot de passe), et non la requête reçue
        UtilisateurResponseDto cree = utilisateurService.inscrireUtilisateur(utilisateurToInsert);

        return new ResponseEntity<>(cree, HttpStatus.CREATED);
    }


    @PostMapping("/api/login")
    public ResponseEntity<String>  login (
            @RequestBody LoginRequestDto request) {

        return authService.login(request);

    }
}
