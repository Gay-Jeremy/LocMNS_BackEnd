package com.locmns.locmns_backend.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.MaterielDao;
import com.locmns.locmns_backend.model.Materiel;
import com.locmns.locmns_backend.services.MaterielService;
import com.locmns.locmns_backend.view.MaterielView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/materiels")
@RequiredArgsConstructor
public class MaterielController {

    @Autowired
    protected MaterielDao materielDao;

    protected final MaterielService materielService;

    @JsonView(MaterielView.class)
    @GetMapping("/liste")
    public List<Materiel> getAll() {

        return materielService.findAll();
    }

    @JsonView(MaterielView.class)
    @GetMapping("/liste/disponible")
    public List<Materiel> getMaterielDisponible() {

        return materielService.findDisponible();
    }

    @JsonView(MaterielView.class)
    @GetMapping("/liste/disponible/{dateDebut}/{dateFin}")
    public List<Materiel> getMaterielDisponiblePeriode(@PathVariable LocalDate dateDebut,
                                                       @PathVariable LocalDate dateFin) {

        return materielService.findDisponiblePeriode(dateDebut, dateFin);
    }



    @JsonView(MaterielView.class)
    @GetMapping("/{id}")
    public ResponseEntity<Materiel> getById(@PathVariable int id) {

        Optional<Materiel> optionalMateriel = materielDao.findById(id);

        if (optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalMateriel.get(), HttpStatus.OK);
    }


    @PostMapping("")
    public Materiel create(@RequestBody Materiel materielToInsert ) {

        materielToInsert.setId(null);

        materielDao.save(materielToInsert);

        return materielToInsert;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        Optional<Materiel> optionalMateriel = materielDao.findById(id);

        if(optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        materielDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update (@PathVariable int id, @RequestBody Materiel materielToInsert) {

        Optional<Materiel> optionalMateriel = materielDao.findById(id);

        if(optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        materielDao.save(materielToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
