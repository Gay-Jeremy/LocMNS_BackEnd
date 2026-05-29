package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.repository.StatutMaterielRepository;
import com.locmns.locmns_backend.model.StatutMateriel;
import com.locmns.locmns_backend.view.StatutMaterielView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/statuts_materiel")
@RequiredArgsConstructor
public class StatutMaterielController {

    private final StatutMaterielRepository statutMaterielRepository;

    @GetMapping("/liste")
    @JsonView({ StatutMaterielView.class})
    public List<StatutMateriel> obtenirTousLesStatutsMateriel() {
        return statutMaterielRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutMateriel> obtenirStatutMaterielParId(@PathVariable int id) {
        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielRepository.findById(id);
        if(optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalStatutMateriel.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<StatutMateriel>  creerStatutMateriel(
            @RequestBody @Valid StatutMateriel statutMaterielToInsert) {

        statutMaterielRepository.save(statutMaterielToInsert);

        return new ResponseEntity<>(statutMaterielToInsert, HttpStatus.CREATED) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerStatutMateriel(@PathVariable int id) {
        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielRepository.findById(id);
        if (optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        statutMaterielRepository.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierStatutMateriel(@PathVariable int id,
                                                       @RequestBody @Valid StatutMateriel statutMaterielToUpdate) {

        statutMaterielToUpdate.setId(id);

        Optional<StatutMateriel> optionalStatutMateriel = statutMaterielRepository.findById(id);
        if (optionalStatutMateriel.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        statutMaterielRepository.save(statutMaterielToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
