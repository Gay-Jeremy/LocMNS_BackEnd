package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.StatutEmpruntDao;
import com.locmns.locmns_backend.model.StatutEmprunt;
import com.locmns.locmns_backend.view.StatutEmpruntView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/statuts_emprunt")
public class StatutEmpruntController {

    @Autowired
    protected StatutEmpruntDao statutEmpruntDao;

    @GetMapping("/liste")
    @JsonView(StatutEmpruntView.class)
    public List<StatutEmprunt> getAll() {
        return statutEmpruntDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutEmprunt> get(@PathVariable int id) {
        Optional<StatutEmprunt> optionalStatutEmprunt = statutEmpruntDao.findById(id);
        if(optionalStatutEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalStatutEmprunt.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public StatutEmprunt create(@RequestBody StatutEmprunt statutEmpruntToInsert) {
        statutEmpruntToInsert.setId(null);
        statutEmpruntDao.save(statutEmpruntToInsert);
        return statutEmpruntToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<StatutEmprunt> optionalStatutEmprunt = statutEmpruntDao.findById(id);
        if (optionalStatutEmprunt.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        statutEmpruntDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id,
                                       @RequestBody StatutEmprunt statutEmpruntToUpdate) {

        statutEmpruntToUpdate.setId(id);

        Optional<StatutEmprunt> optionalStatutEmprunt = statutEmpruntDao.findById(id);
        if (optionalStatutEmprunt.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        statutEmpruntDao.save(statutEmpruntToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
