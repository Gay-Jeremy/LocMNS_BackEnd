package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.EmpruntDao;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.view.EmpruntView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
public class EmpruntController {
    @Autowired
    protected EmpruntDao empruntDao;

    private final EmpruntService empruntService;

    @GetMapping("/liste/recent")
    @JsonView(EmpruntView.class)
    public List<Emprunt> getTop5Emprunt() {
        return empruntService.obtenirEmpruntsRecents();
    }

    @GetMapping("/liste/actif")
    @JsonView(EmpruntView.class)
    public List<Emprunt> getEmpruntActif() {
        return empruntService.obtenirEmpruntsActifs();
    }

    @GetMapping("/liste")
    @JsonView(EmpruntView.class)
    public List<Emprunt> getAll() {
        return empruntService.obtenirTousLesEmprunts();
    }


    @GetMapping("/liste/demande")
    @JsonView(EmpruntView.class)
    public List<Emprunt> getEmpruntDemande() {
        return empruntService.obtenirDemandesEmprunt();
    }

    @GetMapping("/liste/panne")
    @JsonView(EmpruntView.class)
    public List<Emprunt> getEmpruntPanne() {
        return empruntService.obtenirPannesSignalees();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Emprunt> getById(@PathVariable int id) {

        Optional<Emprunt> optionalEmprunt = empruntDao.findById(id);

        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalEmprunt.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public Emprunt create(@RequestBody Emprunt empruntToInsert) {

        empruntToInsert.setId(null);

        empruntDao.save(empruntToInsert);

        return empruntToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        Optional<Emprunt> optionalEmprunt = empruntDao.findById(id);

        if (optionalEmprunt.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        empruntDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Emprunt empruntToInsert ) {

        empruntToInsert.setId(id);

        Optional<Emprunt> optionalEmprunt = empruntDao.findById(id);

        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        empruntDao.save(empruntToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
