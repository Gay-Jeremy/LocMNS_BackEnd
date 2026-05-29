package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.services.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final IUtilisateurService IUtilisateurService;

    @GetMapping("/liste")
    public List<UtilisateurResponseDto> obtenirTousLesUtilisateurs() {
        return IUtilisateurService.obtenirTousLesUtilisateurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDto> obtenirUtilisateurParId(@PathVariable int id) {

        Optional<UtilisateurResponseDto> optionalUtilisateur = IUtilisateurService.obtenirUtilisateurParId(id);

        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUtilisateur.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<UtilisateurResponseDto> creerUtilisateur(
            @RequestBody @Validated(Utilisateur.OnCreate.class) UtilisateurRequestDto utilisateurToInsert) {

        UtilisateurResponseDto utilisateur = IUtilisateurService.creerUtilisateur(utilisateurToInsert);

        return new ResponseEntity<UtilisateurResponseDto>(utilisateur, HttpStatus.CREATED) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable int id) {
        IUtilisateurService.supprimerUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierUtilisateur(@PathVariable int id, @RequestBody @Validated(Utilisateur.OnUpdate.class) UtilisateurRequestDto utilisateurToInsert ) {

        IUtilisateurService.modifierUtilisateur(id, utilisateurToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
