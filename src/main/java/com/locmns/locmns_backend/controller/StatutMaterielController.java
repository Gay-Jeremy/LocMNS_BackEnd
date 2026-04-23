package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.StatutMaterielDao;
import com.locmns.locmns_backend.model.StatutMateriel;
import com.locmns.locmns_backend.view.MaterielView;
import com.locmns.locmns_backend.view.StatutEmpruntView;
import com.locmns.locmns_backend.view.StatutMaterielView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/statust_materiel")
public class StatutMaterielController {

    @Autowired
    protected StatutMaterielDao statutMaterielDao;

    @GetMapping("/liste")
    @JsonView({MaterielView.class, StatutMaterielView.class})
    public List<StatutMateriel> obtenirTousLesStatutsMateriel() {
        return statutMaterielDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutMateriel> obtenirStatutMaterielParId(@PathVariable int id) {
        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielDao.findById(id);
        if(optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalStatutMateriel.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public StatutMateriel creerStatutMateriel(@RequestBody StatutMateriel statutMaterielToInsert) {
        statutMaterielToInsert.setId(null);
        statutMaterielDao.save(statutMaterielToInsert);
        return statutMaterielToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerStatutMateriel(@PathVariable int id) {
        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielDao.findById(id);
        if (optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        statutMaterielDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierStatutMateriel(@PathVariable int id,
                                                       @RequestBody StatutMateriel statutMaterielToUpdate) {

        statutMaterielToUpdate.setId(id);

        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielDao.findById(id);
        if (optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        statutMaterielDao.save(statutMaterielToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
