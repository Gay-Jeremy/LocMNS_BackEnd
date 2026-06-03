package com.locmns.locmns_backend.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dto.request.ModeleRequestDto;
import com.locmns.locmns_backend.repository.ModeleRepository;
import com.locmns.locmns_backend.model.Modele;
import com.locmns.locmns_backend.services.IModeleService;
import com.locmns.locmns_backend.view.ModeleView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api/modeles")
@RequiredArgsConstructor
public class ModeleController {

    private final ModeleRepository modeleRepository;

    private final IModeleService IModeleService;

    @JsonView(ModeleView.class)
    @GetMapping("/liste")
    public List<Modele> obtenirTousLesModeles() {

        return modeleRepository.findAll();
    }

    @JsonView(ModeleView.class)
    @GetMapping("/liste/categorie/{categorieModeleId}")
    public List<Modele> obtenirLesModelesParIdCategorieModele(@PathVariable int categorieModeleId) {
        return modeleRepository.findByCategorieModeleId(categorieModeleId);
    }

    @JsonView(ModeleView.class)
    @GetMapping("/{id}")
    public ResponseEntity<Modele> obtenirModeleParId(@PathVariable int id) {

        Optional<Modele> optionalModele = modeleRepository.findById(id);

        if (optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalModele.get(), HttpStatus.OK);
    }

    @JsonView(ModeleView.class)
    @PostMapping("")
    public ResponseEntity<Modele> creerModele(@RequestBody @Valid ModeleRequestDto modeleRequestDto) {
        Modele modele = IModeleService.creerModele(modeleRequestDto);
        return new ResponseEntity<>(modele, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerModele(@PathVariable int id) {

        Optional<Modele> optionalModele = modeleRepository.findById(id);

        if(optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        modeleRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierModele(@PathVariable int id, @RequestBody @Valid Modele modeleToInsert) {

        modeleToInsert.setId(id);

        Optional<Modele> optionalModele = modeleRepository.findById(id);

        if(optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        modeleRepository.save(modeleToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
