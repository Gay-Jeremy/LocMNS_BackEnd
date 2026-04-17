package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.DroitEmpruntDao;
import com.locmns.locmns_backend.model.Documentation;
import com.locmns.locmns_backend.model.DroitEmprunt;
import com.locmns.locmns_backend.view.DroitEmpruntView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/droit_emprunt")
public class DroitEmpruntController {
    
    @Autowired
    protected DroitEmpruntDao droitEmpruntDao;

    @GetMapping("/liste")
    @JsonView(DroitEmpruntView.class)
    public List<DroitEmprunt> obtenirTousLesDroitsEmprunt() {

        return droitEmpruntDao.findAll();
    }

    @GetMapping("/{id_categorie_modele}")
    @JsonView(DroitEmpruntView.class)
    public List<DroitEmprunt> obtenirTousLesDroitsEmpruntParIdCategorieModele(@PathVariable int id_categorie_modele) {

        return droitEmpruntDao.findByCategorieUtilisateurId(id_categorie_modele);
    }


}
