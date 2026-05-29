package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.repository.StatutEmpruntRepository;
import com.locmns.locmns_backend.model.StatutEmprunt;
import com.locmns.locmns_backend.view.StatutEmpruntView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/statuts_emprunt")
@RequiredArgsConstructor
public class StatutEmpruntController {

    private final StatutEmpruntRepository statutEmpruntRepository;

    @GetMapping("/liste")
    @JsonView(StatutEmpruntView.class)
    public List<StatutEmprunt> obtenirTousLesStatutsEmprunt() {
        return statutEmpruntRepository.findAll();
    }

    @GetMapping("/liste/decision")
    @JsonView(StatutEmpruntView.class)
    public List<StatutEmprunt> obtenirToutesLesDecisions() {
        return statutEmpruntRepository.retourneDecisionDemandeEmprunt();
    }


    @GetMapping("/{id}")
    public ResponseEntity<StatutEmprunt> obtenirStatutEmpruntParId(@PathVariable int id) {
        Optional<StatutEmprunt> optionalStatutEmprunt = statutEmpruntRepository.findById(id);
        if(optionalStatutEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalStatutEmprunt.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<StatutEmprunt>  creerStatutEmprunt(
            @RequestBody @Valid StatutEmprunt statutEmpruntToInsert) {

        statutEmpruntRepository.save(statutEmpruntToInsert);

        return new ResponseEntity<>(statutEmpruntToInsert, HttpStatus.CREATED) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerStatutEmprunt(@PathVariable int id) {
        Optional<StatutEmprunt> optionalStatutEmprunt = statutEmpruntRepository.findById(id);
        if (optionalStatutEmprunt.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        statutEmpruntRepository.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierStatutEmprunt(@PathVariable int id,
                                                      @RequestBody @Valid StatutEmprunt statutEmpruntToUpdate) {

        statutEmpruntToUpdate.setId(id);

        Optional<StatutEmprunt> optionalStatutEmprunt = statutEmpruntRepository.findById(id);
        if (optionalStatutEmprunt.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        statutEmpruntRepository.save(statutEmpruntToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
