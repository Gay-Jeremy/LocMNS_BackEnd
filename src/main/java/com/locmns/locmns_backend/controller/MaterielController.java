package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.MaterielDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.dto.response.MaterielResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.repository.EmpruntRepository;
import com.locmns.locmns_backend.repository.MaterielRepository;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.MaterielService;
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

    private final MaterielRepository materielRepository;
    private final EmpruntRepository empruntRepository;
    private final MaterielService materielService;

    @GetMapping("/liste")
    @IsUser
    public List<MaterielResponseDto> obtenirTousLesMateriels(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return materielService.obtenirMaterielsSelonRole(utilisateur);
    }

    @GetMapping("/liste/disponible")
    @IsUser
    public List<MaterielResponseDto> obtenirMaterielsDisponibles() {
        return materielService.obtenirMaterielsDisponibles();
    }

    @GetMapping("/liste/disponible/categorie")
    @IsUser
    public List<MaterielResponseDto> obtenirMateriels(
            @RequestParam LocalDate dateDebut,
            @RequestParam LocalDate dateFin,
            @RequestParam int categorieModeleId) {
        return materielService.obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(dateDebut, dateFin, categorieModeleId);
    }

    @GetMapping("/liste/panne_signalee")
    @IsUser
    public List<MaterielResponseDto> obtenirListeMaterielPanneSignalee(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return materielService.obtenirListeMaterielPanneSignaleeSelonRole(utilisateur);
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<MaterielResponseDto> obtenirMaterielParId(@PathVariable int id) {
        Optional<MaterielResponseDto> optionalMateriel = materielService.obtenirMaterielParId(id);
        if (optionalMateriel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalMateriel.get(), HttpStatus.OK);
    }

    @PostMapping("")
    @IsAdmin
    public ResponseEntity<MaterielResponseDto> creerMateriel(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails,
            @RequestBody @Valid MaterielDto materielDto) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        MaterielResponseDto materiel = materielService.creerMateriel(materielDto, utilisateur);
        return new ResponseEntity<>(materiel, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerMateriel(@PathVariable int id) {
        if (materielRepository.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (empruntRepository.existsEmpruntActifParMaterielId(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        empruntRepository.deleteByMaterielId(id);
        materielRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierMateriel(
            @PathVariable int id,
            @RequestBody @Valid MaterielDto materielDto) {
        materielService.modifierMateriel(id, materielDto);
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