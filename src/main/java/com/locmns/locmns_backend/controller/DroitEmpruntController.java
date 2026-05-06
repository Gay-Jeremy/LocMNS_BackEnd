package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.DroitEmpruntDao;
import com.locmns.locmns_backend.model.Documentation;
import com.locmns.locmns_backend.model.DroitEmprunt;
import com.locmns.locmns_backend.view.DroitEmpruntView;
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
@RequestMapping("/api/droits_emprunt")
@RequiredArgsConstructor
public class DroitEmpruntController {

    private final DroitEmpruntDao droitEmpruntDao;

    @GetMapping("/liste")
    @JsonView(DroitEmpruntView.class)
    public List<DroitEmprunt> obtenirTousLesDroitsEmprunt() {

        return droitEmpruntDao.findAll();
    }

    @GetMapping("/{id_categorie_utilisateur}")
    @JsonView(DroitEmpruntView.class)
    public List<DroitEmprunt> obtenirTousLesDroitsEmpruntParIdCategorieUtilisateur(@PathVariable int id_categorie_utilisateur) {

        return droitEmpruntDao.findByCategorieUtilisateurId(id_categorie_utilisateur);
    }

    @PostMapping("")
    public ResponseEntity<Void> creerDroitEmprunt(@RequestBody @Valid DroitEmprunt droitEmpruntToInsert) {
        droitEmpruntDao.save(droitEmpruntToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{categorieUtilisateurId}/{categorieModeleId}")
    public ResponseEntity<Void> supprimerDroitEmprunt(@PathVariable int categorieUtilisateurId,
                                                      @PathVariable int categorieModeleId) {
        DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele cle =
                new DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele(categorieUtilisateurId,
                        categorieModeleId);

        if (!droitEmpruntDao.existsById(cle)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        droitEmpruntDao.deleteById(cle);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
