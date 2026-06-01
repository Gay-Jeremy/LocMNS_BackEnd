package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.IEmpruntService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
public class EmpruntController {

    private final IEmpruntService empruntService;

    // =========================
    // UTILS
    // =========================

    private Utilisateur getUser(UtilisateurDetails details) {
        return details.getUtilisateur();
    }

    // =========================
    // READ
    // =========================

    @GetMapping("/liste")
    public List<EmpruntResponseDto> all() {
        return empruntService.obtenirTousLesEmprunts();
    }

    @GetMapping("/{id}")
    public EmpruntResponseDto getById(@PathVariable int id) {
        return empruntService.obtenirEmpruntsParId(id)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable"));
    }

    @GetMapping("/liste/actif")
    public List<EmpruntResponseDto> actifs(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirEmpruntSelonRole(getUser(user));
    }

    @GetMapping("/liste/demande")
    public List<EmpruntResponseDto> demandes(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirDemandesEmpruntSelonRole(getUser(user));
    }

    @GetMapping("/liste/recent")
    public List<EmpruntResponseDto> recent(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirEmpruntsRecentsSelonRole(getUser(user));
    }

    @GetMapping("/liste/retard")
    public List<EmpruntResponseDto> retard(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirListeEmpruntEnRetardSelonRole(getUser(user));
    }

    @GetMapping("/historique")
    public List<EmpruntResponseDto> historique(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirHistoriqueEmpruntSelonRole(getUser(user));
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public EmpruntResponseDto create(@RequestBody @Valid EmpruntRequestDto dto) {
        return empruntService.creerEmprunt(dto);
    }

    // =========================
    // UPDATE EMPRUNT
    // =========================

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody @Valid EmpruntRequestDto dto) {
        empruntService.modifierEmprunt(id, dto);
    }

    // =========================
    // CHANGEMENT DE STATUT SIMPLE (ACCEPTE, REFUSE, TERMINE...)
    // =========================

    @PatchMapping("/{id}/statut")
    public void updateStatut(
            @PathVariable int id,
            @RequestBody StatutUpdateRequestDto dto
    ) {
        empruntService.modifierStatutEmprunt(id, dto);
    }

    // =========================
    // DEMANDE PROLONGATION (utilisateur)
    // =========================

    @PatchMapping("/{id}/prolongation/demande")
    public void demanderProlongation(
            @PathVariable int id,
            @RequestBody DemandeProlongationRequestDto dto
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
            @RequestBody DemandeRetourAnticipeRequestDto dto
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
    public void delete(@PathVariable int id) {
        empruntService.supprimerEmprunt(id);
    }
}