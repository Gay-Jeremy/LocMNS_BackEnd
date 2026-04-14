package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.UtilisateurDao;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.view.UtilisateurView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    protected UtilisateurDao utilisateurDao;

    @GetMapping("liste")
    @JsonView( {UtilisateurView.class})
    public List<Utilisateur> getAll() {
        return utilisateurDao.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Utilisateur.class)
    public ResponseEntity<Utilisateur> getById(@PathVariable int id) {

        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUtilisateur.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public Utilisateur create(@RequestBody Utilisateur utilisateurToInsert) {

        utilisateurToInsert.setId(null);

        utilisateurDao.save(utilisateurToInsert);

        return utilisateurToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        if (optionalUtilisateur.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        utilisateurDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Utilisateur utilisateurToInsert ) {

        utilisateurToInsert.setId(id);

        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        utilisateurDao.save(utilisateurToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
