package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.repository.UtilisateurDao;
import com.locmns.locmns_backend.view.UtilisateurView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UtilisateurDao utilisateurDao;

    @PostMapping("")
    @JsonView( UtilisateurView.class )
    public ResponseEntity<Utilisateur>  signIn(
            @RequestBody @Validated(Utilisateur.OnCreate.class) Utilisateur utilisateurToInsert) {

        utilisateurDao.save(utilisateurToInsert);

        return new ResponseEntity<>(utilisateurToInsert,HttpStatus.CREATED);
    }
}
