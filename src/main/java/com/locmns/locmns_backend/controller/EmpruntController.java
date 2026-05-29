package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import com.locmns.locmns_backend.dto.request.StatutRequestDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.IsUser;
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

    private final IEmpruntService empruntService;

    @GetMapping("/liste/recent")
    public List<EmpruntResponseDto> obtenirEmpruntsRecents(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return empruntService.obtenirEmpruntsRecentsSelonRole(utilisateur);
    }

    @GetMapping("/liste/actif")
    public List<EmpruntResponseDto> obtenirEmpruntActif(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return empruntService.obtenirEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/liste")
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return empruntService.obtenirTousLesEmprunts();
    }

    @GetMapping("/liste/demande")
    public List<EmpruntResponseDto> obtenirDemandesEmprunt(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return empruntService.obtenirDemandesEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/liste/retard")
    public List<EmpruntResponseDto> obtenirEmpruntEnRetard(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return empruntService.obtenirListeEmpruntEnRetardSelonRole(utilisateur);
    }

    @GetMapping("/historique")
    public List<EmpruntResponseDto> obtenirHistoriqueEmprunt(
            @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        Utilisateur utilisateur = utilisateurDetails.getUtilisateur();
        return empruntService.obtenirHistoriqueEmpruntSelonRole(utilisateur);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpruntResponseDto> obtenirEmpruntParId(@PathVariable int id) {
        Optional<EmpruntResponseDto> optionalEmprunt = empruntService.obtenirEmpruntsParId(id);
        if (optionalEmprunt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalEmprunt.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<EmpruntResponseDto> creerEmprunt(@RequestBody @Valid EmpruntRequestDto empruntRequestDto) {
        EmpruntResponseDto emprunt = empruntService.creerEmprunt(empruntRequestDto);
        return new ResponseEntity<>(emprunt, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmprunt(@PathVariable int id) {
        empruntService.supprimerEmprunt(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierEmprunt(@PathVariable int id, @RequestBody @Valid EmpruntRequestDto empruntRequestDto) {
        empruntService.modifierEmprunt(id, empruntRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Void> modifierStatutEmprunt(
            @PathVariable int id,
            @RequestBody StatutRequestDto statutRequestDTO) {
        empruntService.modifierStatutEmpruntFromDto(id, statutRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}