package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.EmpruntDao;
import com.locmns.locmns_backend.dao.MaterielDao;
import com.locmns.locmns_backend.dao.StatutEmpruntDao;
import com.locmns.locmns_backend.dao.UtilisateurDao;
import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.model.Materiel;
import com.locmns.locmns_backend.model.StatutEmprunt;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.services.EmpruntService;
import com.locmns.locmns_backend.view.EmpruntView;
import jakarta.validation.Valid;
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
    @JsonView(EmpruntView.class)
    public ResponseEntity<Emprunt> obtenirEmpruntParId(@PathVariable int id) {

        Optional<Emprunt> optionalEmprunt = empruntService.obtenirEmpruntsParId(id);

        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalEmprunt.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> creerEmprunt(@RequestBody @Valid EmpruntDto empruntDto) {

        empruntService.creerEmprunt(empruntDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmprunt(@PathVariable int id) {
        empruntService.supprimerEmprunt(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierEmprunt(@PathVariable int id, @RequestBody @Valid Emprunt empruntToInsert) {
        empruntService.modifierEmprunt(id, empruntToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
