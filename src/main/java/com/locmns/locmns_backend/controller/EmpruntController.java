package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.IEmpruntService;
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
    public List<EmpruntResponseDto> obtenirEmpruntsRecents(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirEmpruntsRecentsSelonRole(utilisateur);
    }

    @GetMapping("/liste/actif")
    public List<EmpruntResponseDto> obtenirEmpruntActif(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/liste")
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return IEmpruntService.obtenirTousLesEmprunts();
    }

    @GetMapping("/liste/demande")
    public List<EmpruntResponseDto> obtenirDemandesEmprunt(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirDemandesEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/liste/retard")
    public List<EmpruntResponseDto> obtenirEmpruntEnRetard(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirListeEmpruntEnRetardSelonRole(utilisateur);
    }

    @GetMapping("/historique")
    public List<EmpruntResponseDto> obtenirHistoriqueEmprunt(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return IEmpruntService.obtenirHistoriqueEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpruntResponseDto> obtenirEmpruntParId(@PathVariable int id) {
        Optional<EmpruntResponseDto> optionalEmprunt = IEmpruntService.obtenirEmpruntsParId(id);
        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalEmprunt.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<EmpruntResponseDto> creerEmprunt(@RequestBody @Valid EmpruntDto empruntDto) {
        EmpruntResponseDto emprunt = IEmpruntService.creerEmprunt(empruntDto);
        return new ResponseEntity<>(emprunt, HttpStatus.CREATED);
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