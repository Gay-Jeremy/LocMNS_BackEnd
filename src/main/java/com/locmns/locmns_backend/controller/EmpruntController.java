package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.IEmpruntService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
public class EmpruntController {

    private final IEmpruntService empruntService;

    // =========================
    // UTILS
    // =========================

    private Utilisateur obtenirUtilisateur(UtilisateurDetails details) {
        return details.getUtilisateur();
    }

    // =========================
    // READ
    // =========================

    @GetMapping("/liste")
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return empruntService.obtenirTousLesEmprunts();
    }

    @GetMapping("/{id}")
    public EmpruntResponseDto obtenirEmpruntParId(@PathVariable int id) {
        return empruntService.obtenirEmpruntsParId(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt introuvable"));
    }

    @GetMapping("/liste/actif")
    public List<EmpruntResponseDto> actifs(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirEmpruntSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/liste/demande")
    public List<EmpruntResponseDto> demandes(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirDemandesEmpruntSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/liste/recent")
    public List<EmpruntResponseDto> recent(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirEmpruntsRecentsSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/liste/retard")
    public List<EmpruntResponseDto> retard(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirListeEmpruntEnRetardSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/historique")
    public List<EmpruntResponseDto> historique(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirHistoriqueEmpruntSelonRole(obtenirUtilisateur(user));
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public EmpruntResponseDto creer(@RequestBody @Valid EmpruntRequestDto dto, @AuthenticationPrincipal UtilisateurDetails utilisateurDetails) {
        return empruntService.creerEmprunt(dto, obtenirUtilisateur(utilisateurDetails));
    }

    // =========================
    // UPDATE EMPRUNT
    // =========================

    @PutMapping("/{id}")
    public void mettreAjour(@PathVariable int id, @RequestBody @Valid EmpruntRequestDto dto) {
        empruntService.modifierEmprunt(id, dto);
    }

    // =========================
    // CHANGEMENT DE STATUT SIMPLE (ACCEPTE, REFUSE, TERMINE...)
    // =========================

    @PatchMapping("/{id}/statut")
    public void mettreAjourStatut(
            @PathVariable int id,
            @RequestBody @Valid EmpruntStatutRequestDto dto
    ) {
        empruntService.modifierStatutEmprunt(id, dto);
    }

    // =========================
    // DEMANDE PROLONGATION (utilisateur)
    // =========================

    @PatchMapping("/{id}/prolongation/demande")
    public void demanderProlongation(
            @PathVariable int id,
            @RequestBody @Valid DemandeProlongationRequestDto dto
    ) {
        empruntService.demanderProlongation(id, dto);
    }

    // =========================
    // ACCEPTER PROLONGATION (admin)
    // =========================

    @PatchMapping("/{id}/prolongation/accepter")
    public void accepterProlongation(@PathVariable int id) {
        empruntService.accepterProlongation(id);
    }

    // =========================
    // DEMANDE RETOUR ANTICIPÉ (utilisateur)
    // =========================

    @PatchMapping("/{id}/retour-anticipe/demande")
    public void demanderRetourAnticipe(
            @PathVariable int id,
            @RequestBody @Valid DemandeRetourAnticipeRequestDto dto
    ) {
        empruntService.demanderRetourAnticipe(id, dto);
    }

    // =========================
    // ACCEPTER RETOUR ANTICIPÉ (admin)
    // =========================

    @PatchMapping("/{id}/retour-anticipe/accepter")
    public void accepterRetourAnticipe(@PathVariable int id) {
        empruntService.accepterRetourAnticipe(id);
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    public void supprimerParId(@PathVariable int id) {
        empruntService.supprimerEmprunt(id);
    }
}
