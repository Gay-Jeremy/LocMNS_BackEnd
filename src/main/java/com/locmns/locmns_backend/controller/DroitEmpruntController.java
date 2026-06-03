package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.repository.DroitEmpruntRepository;
import com.locmns.locmns_backend.model.DroitEmprunt;
import com.locmns.locmns_backend.view.DroitEmpruntView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/droits_emprunt")
@RequiredArgsConstructor
public class DroitEmpruntController {

    private final DroitEmpruntRepository droitEmpruntRepository;

    @GetMapping("/liste")
    @JsonView(DroitEmpruntView.class)
    public List<DroitEmprunt> obtenirTousLesDroitsEmprunt() {

        return droitEmpruntRepository.findAll();
    }

    @GetMapping("/{idCategorieUtilisateur}")
    @JsonView(DroitEmpruntView.class)
    public List<DroitEmprunt> obtenirTousLesDroitsEmpruntParIdCategorieUtilisateur(@PathVariable int idCategorieUtilisateur) {

        return droitEmpruntRepository.findByCategorieUtilisateurId(idCategorieUtilisateur);
    }

    @PostMapping("")
    public ResponseEntity<Void> creerDroitEmprunt(@RequestBody @Valid DroitEmprunt droitEmpruntToInsert) {
        droitEmpruntRepository.save(droitEmpruntToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{categorieUtilisateurId}/{categorieModeleId}")
    public ResponseEntity<Void> supprimerDroitEmprunt(@PathVariable int categorieUtilisateurId,
                                                      @PathVariable int categorieModeleId) {
        DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele cle =
                new DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele(categorieUtilisateurId,
                        categorieModeleId);

        if (!droitEmpruntRepository.existsById(cle)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        droitEmpruntRepository.deleteById(cle);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
