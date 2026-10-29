package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.CategorieUtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieUtilisateurResumeDto;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
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
    @IsUser
    public List<CategorieUtilisateurResumeDto> obtenirToutesLesCategoriesUtilisateur() {
        return categorieUtilisateurService.obtenirToutesLesCategoriesUtilisateur();
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<CategorieUtilisateurResumeDto> obtenirCategorieUtilisateurParId(@PathVariable int id) {
        return categorieUtilisateurService.obtenirCategorieUtilisateurParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<CategorieUtilisateurResumeDto> creerCategorieUtilisateur(
            @RequestBody @Valid CategorieUtilisateurRequestDto dto) {
        return new ResponseEntity<>(categorieUtilisateurService.creerCategorieUtilisateur(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerCategorieUtilisateur(@PathVariable int id) {
        categorieUtilisateurService.supprimerCategorieUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierCategorieUtilisateur(
            @PathVariable int id,
            @RequestBody @Valid CategorieUtilisateurRequestDto dto) {
        categorieUtilisateurService.modifierCategorieUtilisateur(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
