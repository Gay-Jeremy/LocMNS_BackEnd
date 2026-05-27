package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.IEmpruntService;
import com.locmns.locmns_backend.view.EmpruntView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
public class EmpruntController {

    private final IEmpruntService IEmpruntService;

    @GetMapping("/liste/recent")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirEmpruntsRecents(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {

        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();

        return IEmpruntService.obtenirEmpruntsRecentsSelonRole(utilisateur);
    }

    @GetMapping("/liste/actif")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirEmpruntActif(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {

        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/liste")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirTousLesEmprunts() {
        return IEmpruntService.obtenirTousLesEmprunts();
    }


    @GetMapping("/liste/demande")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirDemandesEmprunt(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {

        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirDemandesEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/liste/retard")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirEmpruntEnRetard(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {

        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirListeEmpruntEnRetardSelonRole(utilisateur);
    }

    @GetMapping("/historique")
    @JsonView(EmpruntView.class)
    public List<Emprunt> obtenirHistoriqueEmprunt(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {

        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirHistoriqueEmpruntSelonRole(utilisateur);}


    @GetMapping("/{id}")
    @JsonView(EmpruntView.class)
    public ResponseEntity<Emprunt> obtenirEmpruntParId(@PathVariable int id) {

        Optional<Emprunt> optionalEmprunt = IEmpruntService.obtenirEmpruntsParId(id);

        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalEmprunt.get(), HttpStatus.OK);
    }

    @JsonView(EmpruntView.class)
    @PostMapping("")
    public ResponseEntity<Emprunt> creerEmprunt(@RequestBody @Valid EmpruntDto empruntDto) {
        Emprunt emprunt = IEmpruntService.creerEmprunt(empruntDto);
        return new ResponseEntity<>(emprunt,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmprunt(@PathVariable int id) {
        IEmpruntService.supprimerEmprunt(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierEmprunt(@PathVariable int id, @RequestBody @Valid EmpruntDto empruntDto) {
        IEmpruntService.modifierEmprunt(id, empruntDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Void> modifierStatutEmprunt(
            @PathVariable int id,
            @RequestBody StatutDto statutDTO) {

        IEmpruntService.modifierStatutEmpruntFromDto(id, statutDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
