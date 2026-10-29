package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.DroitEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.DroitEmpruntResponseDto;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.services.IDroitEmpruntService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/droits_emprunt")
@RequiredArgsConstructor
public class DroitEmpruntController {

    private final IDroitEmpruntService droitEmpruntService;

    @GetMapping("/liste")
    @IsUser
    public List<DroitEmpruntResponseDto> obtenirTousLesDroitsEmprunt() {
        return droitEmpruntService.obtenirTousLesDroitsEmprunt();
    }

    @GetMapping("/{idCategorieUtilisateur}")
    @IsUser
    public List<DroitEmpruntResponseDto> obtenirTousLesDroitsEmpruntParIdCategorieUtilisateur(
            @PathVariable int idCategorieUtilisateur) {
        return droitEmpruntService.obtenirDroitsParCategorieUtilisateur(idCategorieUtilisateur);
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<DroitEmpruntResponseDto> creerDroitEmprunt(
            @RequestBody @Valid DroitEmpruntRequestDto dto) {
        return new ResponseEntity<>(droitEmpruntService.creerDroitEmprunt(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{categorieUtilisateurId}/{categorieModeleId}")
    @IsAdmin
    public ResponseEntity<Void> supprimerDroitEmprunt(
            @PathVariable int categorieUtilisateurId,
            @PathVariable int categorieModeleId) {
        droitEmpruntService.supprimerDroitEmprunt(categorieUtilisateurId, categorieModeleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
