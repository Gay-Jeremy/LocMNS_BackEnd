package com.locmns.locmns_backend.controller;


import com.locmns.locmns_backend.dao.ComposantDao;
import com.locmns.locmns_backend.model.Composant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/composants")
public class ComposantController {

    @Autowired
    protected ComposantDao composantDao;

    @GetMapping("/liste")
    public List<Composant> obtenirTousLesComposants() {

        return composantDao.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Composant> obtenirComposantParId(@PathVariable int id) {

        Optional<Composant> optionalComposant = composantDao.findById(id);

        if(optionalComposant.isEmpty()) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalComposant.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public Composant creerComposant(@RequestBody Composant categorieToInsert) {

        categorieToInsert.setId(null);

        composantDao.save(categorieToInsert);

        return categorieToInsert;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerComposant(@PathVariable int id) {

        Optional<Composant> optionalComposant = composantDao.findById(id);

        if (optionalComposant.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        composantDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierComposant(@PathVariable int id,
                                                  @RequestBody Composant categorieToUpdate) {

        // On écrase l'id du json par celui en paramètre
        categorieToUpdate.setId(id);

        Optional<Composant> optionalComposant = composantDao.findById(id);

        if (optionalComposant.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        composantDao.save(categorieToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }
}
