package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.LoginRequestDto;
import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
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
    public ResponseEntity<UtilisateurRequestDto>  signIn(
            @RequestBody @Validated(ValidationGroups.OnCreate.class) UtilisateurRequestDto utilisateurToInsert) {

        utilisateurService.creerUtilisateur(utilisateurToInsert);

        return new ResponseEntity<>(utilisateurToInsert,HttpStatus.CREATED);
    }


    @PostMapping("/api/login")
    public ResponseEntity<String>  login (
            @RequestBody LoginRequestDto request) {

        return authService.login(request);

    }
}
