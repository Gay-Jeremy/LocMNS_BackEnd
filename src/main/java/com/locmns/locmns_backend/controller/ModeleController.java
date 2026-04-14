package com.locmns.locmns_backend.controller;


import com.locmns.locmns_backend.dao.ModeleDao;
import com.locmns.locmns_backend.model.Modele;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/modele")
public class ModeleController {

    @Autowired
    protected ModeleDao modeleDao;

    @GetMapping("/liste")
    public List<Modele> obtenirTousLesModeles() {

        return modeleDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modele> obtenirModeleParId(@PathVariable int id) {

        Optional<Modele> optionalModele = modeleDao.findById(id);

        if (optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalModele.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public Modele creerModele(@RequestBody Modele modeleToInsert ) {

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
    public ResponseEntity<Void> modifierModele(@PathVariable int id, @RequestBody Modele modeleToInsert) {

        Optional<Modele> optionalModele = modeleDao.findById(id);

        if(optionalModele.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        modeleDao.save(modeleToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
