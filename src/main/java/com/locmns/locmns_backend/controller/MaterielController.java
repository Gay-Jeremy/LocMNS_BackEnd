package com.locmns.locmns_backend.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.MaterielDao;
import com.locmns.locmns_backend.dto.MaterielDto;
import com.locmns.locmns_backend.model.Materiel;
import com.locmns.locmns_backend.services.MaterielService;
import com.locmns.locmns_backend.view.MaterielView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/materiels")
@RequiredArgsConstructor
public class MaterielController {

    private final MaterielDao materielDao;

    private final MaterielService materielService;

    @JsonView(MaterielView.class)
    @GetMapping("/liste")
    public List<Materiel> obtenirTousLesMateriels() {

        return materielService.obtenirTousLesMateriels();
    }

    @JsonView(MaterielView.class)
    @GetMapping("/liste/disponible")
    public List<Materiel> obtenirMaterielsDisponibles() {

        return materielService.obtenirMaterielsDisponibles();
    }


    @JsonView(MaterielView.class)
    @GetMapping("/liste/disponible/{dateDebut}/{dateFin}/{id}")
    public List<Materiel> obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(@PathVariable LocalDate dateDebut,
                                                                                     @PathVariable LocalDate dateFin,
                                                                                     @PathVariable int id) {

        return materielService.obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(dateDebut, dateFin, id);
    }

    @JsonView(MaterielView.class)
    @GetMapping("/liste/panne_signalee")
    public List<Materiel> obtenirListeMaterielPanneSignalee() {
        return materielService.obtenirListeMaterielPanneSignalee();
    }




    @JsonView(MaterielView.class)
    @GetMapping("/{id}")
    public ResponseEntity<Materiel> obtenirMaterielParId(@PathVariable int id) {

        Optional<Materiel> optionalMateriel = materielService.obtenirMaterielParId(id);

        if (optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalMateriel.get(), HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<Materiel>creerMateriel(@RequestBody @Valid MaterielDto materielDto ) {

        Materiel materiel = materielService.creerMateriel(materielDto);
        return new ResponseEntity<>(materiel, HttpStatus.CREATED);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerMateriel(@PathVariable int id) {

        Optional<Materiel> optionalMateriel = materielDao.findById(id);

        if(optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        materielDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierMateriel(@PathVariable int id, @RequestBody @Valid Materiel materielToInsert) {

        materielToInsert.setId(id);

        Optional<Materiel> optionalMateriel = materielDao.findById(id);

        if(optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        materielDao.save(materielToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
