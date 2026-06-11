package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.CategorieUtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieUtilisateurResumeDto;
import com.locmns.locmns_backend.services.ICategorieUtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories_utilisateur")
@RequiredArgsConstructor
public class CategorieUtilisateurController {

    private final ICategorieUtilisateurService categorieUtilisateurService;

    @GetMapping("/liste")
    public List<CategorieUtilisateurResumeDto> obtenirToutesLesCategoriesUtilisateur() {
        return categorieUtilisateurService.obtenirToutesLesCategoriesUtilisateur();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieUtilisateurResumeDto> obtenirCategorieUtilisateurParId(@PathVariable int id) {
        return categorieUtilisateurService.obtenirCategorieUtilisateurParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CategorieUtilisateurResumeDto> creerCategorieUtilisateur(
            @RequestBody @Valid CategorieUtilisateurRequestDto dto) {
        return new ResponseEntity<>(categorieUtilisateurService.creerCategorieUtilisateur(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCategorieUtilisateur(@PathVariable int id) {
        categorieUtilisateurService.supprimerCategorieUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierCategorieUtilisateur(
            @PathVariable int id,
            @RequestBody @Valid CategorieUtilisateurRequestDto dto) {
        categorieUtilisateurService.modifierCategorieUtilisateur(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
