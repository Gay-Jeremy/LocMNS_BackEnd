package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.CategorieUtilisateurDao;
import com.locmns.locmns_backend.model.CategorieUtilisateur;
import com.locmns.locmns_backend.view.CategorieUtilisateurView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/categories_utilisateur")
@RequiredArgsConstructor
public class CategorieUtilisateurController {

    private final CategorieUtilisateurDao categorieUilisateurDao;

    @GetMapping("/liste")
    @JsonView(CategorieUtilisateurView.class)
    public List<CategorieUtilisateur> obtenirToutesLesCategoriesUtilisateur() {

        return categorieUilisateurDao.findAll();
    }

    // Récupère un utilisateur
    @GetMapping("/{id}")

    public ResponseEntity<CategorieUtilisateur> obtenirCategorieUtilisateurParId(@PathVariable int id) {

        Optional<CategorieUtilisateur> optionalCategorieUilisateur = categorieUilisateurDao.findById(id);

        if(optionalCategorieUilisateur.isEmpty()) {

            // return ResponseEntity.notFound().build()
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // return ResponseEntity.ok(optionalUser.get())
        return new ResponseEntity<>(optionalCategorieUilisateur.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public CategorieUtilisateur creerCategorieUtilisateur(@RequestBody @Valid CategorieUtilisateur categorieToInsert) {

        categorieToInsert.setId(null);

        categorieUilisateurDao.save(categorieToInsert);

        return categorieToInsert;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCategorieUtilisateur(@PathVariable int id) {

        Optional<CategorieUtilisateur> optionalCategorieUilisateur = categorieUilisateurDao.findById(id);

        if (optionalCategorieUilisateur.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        categorieUilisateurDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierCategorieUtilisateur(@PathVariable int id,
                                                             @RequestBody @Valid CategorieUtilisateur categorieToUpdate) {

        // On écrase l'id du json par celui en paramètre
        categorieToUpdate.setId(id);

        Optional<CategorieUtilisateur> optionalCategorieUilisateur = categorieUilisateurDao.findById(id);

        if (optionalCategorieUilisateur.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        categorieUilisateurDao.save(categorieToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));

    }
}
