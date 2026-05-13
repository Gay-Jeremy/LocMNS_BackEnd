package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.repository.CategorieModeleDao;
import com.locmns.locmns_backend.model.CategorieModele;
import com.locmns.locmns_backend.view.CategorieModeleView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/categories_modele")
@RequiredArgsConstructor
public class CategorieModeleController {

    private final CategorieModeleDao categorieModeleDao;

    @GetMapping("/liste")
    @JsonView(CategorieModeleView.class)
    public List<CategorieModele> obtenirToutesLesCategoriesModele() {

        return categorieModeleDao.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(CategorieModeleView.class)
    public ResponseEntity<CategorieModele> obtenirCategorieModeleParId(@PathVariable int id) {

        Optional<CategorieModele> optionalCategorieModele = categorieModeleDao.findById(id);

        if(optionalCategorieModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalCategorieModele.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public CategorieModele creerCategorieModele(@RequestBody @Valid CategorieModele categorieToInsert) {

        categorieToInsert.setId(null);

        CategorieModele NouvelleCategorie = categorieModeleDao.save(categorieToInsert);
        
        return NouvelleCategorie;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCategorieModele(@PathVariable int id) {

        Optional<CategorieModele> optionalCategorieModele = categorieModeleDao.findById(id);

        if (optionalCategorieModele.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        categorieModeleDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierCategorieModele(@PathVariable int id,
                                                        @RequestBody  @Valid CategorieModele categorieToUpdate) {

        // On écrase l'id du json par celui en paramètre
        categorieToUpdate.setId(id);

        Optional<CategorieModele> optionalCategorieModele = categorieModeleDao.findById(id);

        if (optionalCategorieModele.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        categorieModeleDao.save(categorieToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));

    }


}
