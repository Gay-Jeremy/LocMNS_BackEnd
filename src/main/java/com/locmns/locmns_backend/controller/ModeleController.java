package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.ModeleRequestDto;
import com.locmns.locmns_backend.dto.response.ModeleResumeDto;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.services.IModeleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modeles")
@RequiredArgsConstructor
public class ModeleController {

    private final IModeleService modeleService;

    @GetMapping("/liste")
    @IsUser
    public List<ModeleResumeDto> obtenirTousLesModeles() {
        return modeleService.obtenirTousLesModeles();
    }

    @GetMapping("/liste/categorie/{categorieModeleId}")
    @IsUser
    public List<ModeleResumeDto> obtenirLesModelesParIdCategorieModele(@PathVariable int categorieModeleId) {
        return modeleService.obtenirModeleParCategorie(categorieModeleId);
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<ModeleResumeDto> obtenirModeleParId(@PathVariable int id) {
        return modeleService.obtenirModeleParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<ModeleResumeDto> creerModele(@RequestBody @Valid ModeleRequestDto dto) {
        return new ResponseEntity<>(modeleService.creerModele(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerModele(@PathVariable int id) {
        modeleService.supprimerModele(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierModele(
            @PathVariable int id,
            @RequestBody @Valid ModeleRequestDto dto) {
        modeleService.modifierModele(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
