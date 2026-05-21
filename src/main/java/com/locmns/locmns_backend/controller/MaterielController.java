package com.locmns.locmns_backend.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.repository.EmpruntDao;
import com.locmns.locmns_backend.repository.MaterielDao;
import com.locmns.locmns_backend.dto.MaterielDto;
import com.locmns.locmns_backend.model.Materiel;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.MaterielService;
import com.locmns.locmns_backend.view.MaterielView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final EmpruntDao empruntDao;

    private final MaterielService materielService;

    @JsonView(MaterielView.class)
    @GetMapping("/liste")
    @IsUser
    public List<Materiel> obtenirTousLesMateriels(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {

        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();

        return materielService.obtenirMaterielsSelonRole(utilisateur);
    }

    @JsonView(MaterielView.class)
    @GetMapping("/liste/disponible")
    @IsUser
    public List<Materiel> obtenirMaterielsDisponibles() {

        return materielService.obtenirMaterielsDisponibles();
    }


    @JsonView(MaterielView.class)
    @GetMapping("/liste/disponible/categorie")
    @IsUser
    public List<Materiel> obtenirMateriels(
            @RequestParam LocalDate dateDebut,
            @RequestParam LocalDate dateFin,
            @RequestParam int categorieModeleId) {

        return materielService.obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(dateDebut, dateFin, categorieModeleId);
    }

    @JsonView(MaterielView.class)
    @IsUser
    @GetMapping("/liste/panne_signalee")
    public List<Materiel> obtenirListeMaterielPanneSignalee(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {

            Utilisateur utilisateur = utilisateurDetails.getUtilisateur();

            return materielService.obtenirListeMaterielPanneSignaleeSelonRole(utilisateur);
        }


    @JsonView(MaterielView.class)
    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<Materiel> obtenirMaterielParId(@PathVariable int id) {

        Optional<Materiel> optionalMateriel = materielService.obtenirMaterielParId(id);

        if (optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalMateriel.get(), HttpStatus.OK);
    }

    @JsonView(MaterielView.class)
    @PostMapping("")
    @IsAdmin
    public ResponseEntity<Materiel>creerMateriel(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails,
            @RequestBody @Valid MaterielDto materielDto ) {

        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();

        Materiel materiel = materielService.creerMateriel(materielDto, utilisateur);
        return new ResponseEntity<>(materiel, HttpStatus.CREATED);

    }


    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerMateriel(@PathVariable int id) {

        if (materielDao.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (empruntDao.existsEmpruntActifParMaterielId(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        empruntDao.deleteByMaterielId(id);
        materielDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @JsonView(MaterielView.class)
    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierMateriel(@PathVariable int id, @RequestBody @Valid Materiel materielToInsert) {

        materielToInsert.setId(id);

        Optional<Materiel> optionalMateriel = materielDao.findById(id);

        if(optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        materielDao.save(materielToInsert);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/statut")
    @IsUser
    public ResponseEntity<Void> modifierStatutMateriel(
            @PathVariable Integer id,
            @RequestBody StatutDto statutDTO) {
        materielService.modifierStatutMateriel(id, statutDTO.getStatutId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
