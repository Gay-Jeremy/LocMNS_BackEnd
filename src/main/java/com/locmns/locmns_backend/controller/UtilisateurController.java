package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.repository.UtilisateurDao;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.view.UtilisateurView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurDao utilisateurDao;

    @GetMapping("/liste")
    @JsonView( {UtilisateurView.class})
    public List<Utilisateur> obtenirTousLesUtilisateurs() {
        return utilisateurDao.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(UtilisateurView.class)
    public ResponseEntity<Utilisateur> obtenirUtilisateurParId(@PathVariable int id) {

        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUtilisateur.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public Utilisateur creerUtilisateur(@RequestBody @Validated(Utilisateur.OnCreate.class) Utilisateur utilisateurToInsert) {

        utilisateurToInsert.setId(null);

        utilisateurDao.save(utilisateurToInsert);

        return utilisateurToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable int id) {

        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        if (optionalUtilisateur.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        utilisateurDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierUtilisateur(@PathVariable int id, @RequestBody @Validated(Utilisateur.OnUpdate.class) Utilisateur utilisateurToInsert ) {

        utilisateurToInsert.setId(id);

        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        utilisateurDao.save(utilisateurToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
