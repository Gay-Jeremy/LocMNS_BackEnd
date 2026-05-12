package com.locmns.locmns_backend.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.repository.ModeleDao;
import com.locmns.locmns_backend.model.Modele;
import com.locmns.locmns_backend.view.ModeleView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/modeles")
@RequiredArgsConstructor
public class ModeleController {

    private final ModeleDao modeleDao;

    @JsonView(ModeleView.class)
    @GetMapping("/liste")
    public List<Modele> obtenirTousLesModeles() {

        return modeleDao.findAll();
    }

    @JsonView(ModeleView.class)
    @GetMapping("/liste/categorie/{categorieModeleId}")
    public List<Modele> obtenirLesModelesParIdCategorieModele(@PathVariable int categorieModeleId) {
        return modeleDao.findByCategorieModeleId(categorieModeleId);
    }

    @JsonView(ModeleView.class)
    @GetMapping("/{id}")
    public ResponseEntity<Modele> obtenirModeleParId(@PathVariable int id) {

        Optional<Modele> optionalModele = modeleDao.findById(id);

        if (optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalModele.get(), HttpStatus.OK);
    }

    @JsonView(ModeleView.class)
    @PostMapping("")
    public Modele creerModele(@RequestBody @Valid Modele modeleToInsert ) {

        modeleToInsert.setId(null);

        modeleDao.save(modeleToInsert);

        return modeleToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerModele(@PathVariable int id) {

        Optional<Modele> optionalModele = modeleDao.findById(id);

        if(optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        modeleDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierModele(@PathVariable int id, @RequestBody @Valid Modele modeleToInsert) {

        modeleToInsert.setId(id);

        Optional<Modele> optionalModele = modeleDao.findById(id);

        if(optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        modeleDao.save(modeleToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
