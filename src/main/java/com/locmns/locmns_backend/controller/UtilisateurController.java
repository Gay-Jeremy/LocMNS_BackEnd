package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.dto.validation.ValidationGroups;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.services.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final IUtilisateurService utilisateurService;

    @GetMapping("/liste")
    @IsAdmin
    public List<UtilisateurResponseDto> obtenirTousLesUtilisateurs() {
        return utilisateurService.obtenirTousLesUtilisateurs();
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<UtilisateurResponseDto> obtenirUtilisateurParId(@PathVariable int id) {

        Optional<UtilisateurResponseDto> optionalUtilisateur = utilisateurService.obtenirUtilisateurParId(id);

        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUtilisateur.get(), HttpStatus.OK);
    }

    @PostMapping("")
    @IsAdmin
    public ResponseEntity<UtilisateurResponseDto> creerUtilisateur(
            @RequestBody @Validated(ValidationGroups.OnCreate.class) UtilisateurRequestDto utilisateurToInsert) {

        UtilisateurResponseDto utilisateur = utilisateurService.creerUtilisateur(utilisateurToInsert);

        return new ResponseEntity<UtilisateurResponseDto>(utilisateur, HttpStatus.CREATED) ;
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable int id) {
        utilisateurService.supprimerUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierUtilisateur(@PathVariable int id, @RequestBody @Validated(ValidationGroups.OnUpdate.class) UtilisateurRequestDto utilisateurToInsert ) {

        utilisateurService.modifierUtilisateur(id, utilisateurToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
