package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dao.SalleDao;
import com.locmns.locmns_backend.model.Salle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/salle")
public class SalleController {

    @Autowired
    protected SalleDao salleDao;

    @GetMapping("/liste")
    public List<Salle> getAll() {
        return salleDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salle> get(@PathVariable int id) {
        Optional<Salle> optionalSalle = salleDao.findById(id);
        if(optionalSalle.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalSalle.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public Salle create(@RequestBody Salle salleToInsert) {
        salleToInsert.setId(null);
        salleDao.save(salleToInsert);
        return salleToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<Salle> optionalSalle = salleDao.findById(id);
        if (optionalSalle.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        salleDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id,
                                       @RequestBody Salle salleToUpdate) {

        salleToUpdate.setId(id);

        Optional<Salle> optionalSalle = salleDao.findById(id);
        if (optionalSalle.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        salleDao.save(salleToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
