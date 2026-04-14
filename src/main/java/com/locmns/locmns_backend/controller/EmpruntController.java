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
    public List<Emprunt> obtenirEmpruntsRecents() {
        return empruntService.obtenirEmpruntsRecents();
    }

    @GetMapping("/liste/actif")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirEmpruntActif() {
        return empruntService.obtenirEmpruntsActifs();
    }

    @GetMapping("/liste")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirTousLesEmprunts() {
        return empruntService.obtenirTousLesEmprunts();
    }


    @GetMapping("/liste/demande")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirDemandesEmprunt() {
        return empruntService.obtenirDemandesEmprunt();
    }

    @GetMapping("/liste/panne")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirPannesSignalees() {
        return empruntService.obtenirPannesSignalees();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Emprunt> obtenirEmpruntParId(@PathVariable int id) {

        Optional<Emprunt> optionalEmprunt = empruntDao.findById(id);

        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalEmprunt.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public Emprunt creerEmprunt(@RequestBody Emprunt empruntToInsert) {

        empruntToInsert.setId(null);

        empruntDao.save(empruntToInsert);

        return empruntToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmprunt(@PathVariable int id) {

        Optional<Emprunt> optionalEmprunt = empruntDao.findById(id);

        if (optionalEmprunt.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        empruntDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierEmprunt(@PathVariable int id, @RequestBody Emprunt empruntToInsert ) {

        empruntToInsert.setId(id);

        Optional<Emprunt> optionalEmprunt = empruntDao.findById(id);

        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        empruntDao.save(empruntToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
