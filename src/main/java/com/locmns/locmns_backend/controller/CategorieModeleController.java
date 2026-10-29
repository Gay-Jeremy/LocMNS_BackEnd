package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.CategorieModeleRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieModeleResumeDto;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.services.ICategorieModeleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories_modele")
@RequiredArgsConstructor
public class CategorieModeleController {

    private final ICategorieModeleService categorieModeleService;

    @GetMapping("/liste")
    @IsUser
    public List<CategorieModeleResumeDto> obtenirToutesLesCategoriesModele() {
        return categorieModeleService.obtenirToutesLesCategoriesModele();
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<CategorieModeleResumeDto> obtenirCategorieModeleParId(@PathVariable int id) {
        return categorieModeleService.obtenirCategorieModeleParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<CategorieModeleResumeDto> creerCategorieModele(
            @RequestBody @Valid CategorieModeleRequestDto dto) {
        return new ResponseEntity<>(categorieModeleService.creerCategorieModele(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerCategorieModele(@PathVariable int id) {
        categorieModeleService.supprimerCategorieModele(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierCategorieModele(
            @PathVariable int id,
            @RequestBody @Valid CategorieModeleRequestDto dto) {
        categorieModeleService.modifierCategorieModele(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
