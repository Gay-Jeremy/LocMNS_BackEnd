package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.StatutMaterielDao;
import com.locmns.locmns_backend.model.StatutMateriel;
import com.locmns.locmns_backend.view.StatutEmpruntView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/statut_materiel")
public class StatutMaterielController {

    @Autowired
    protected StatutMaterielDao statutMaterielDao;

    @GetMapping("/liste")
    @JsonView(StatutEmpruntView.class)
    public List<StatutMateriel> getAll() {
        return statutMaterielDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutMateriel> get(@PathVariable int id) {
        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielDao.findById(id);
        if(optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalStatutMateriel.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public StatutMateriel create(@RequestBody StatutMateriel statutMaterielToInsert) {
        statutMaterielToInsert.setId(null);
        statutMaterielDao.save(statutMaterielToInsert);
        return statutMaterielToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielDao.findById(id);
        if (optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        statutMaterielDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id,
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
