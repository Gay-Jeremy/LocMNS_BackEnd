package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.security.UtilisateurDetails;
import com.locmns.locmns_backend.services.IEmpruntService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @IsUser
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return empruntService.obtenirTousLesEmprunts();
    }

    @GetMapping("/{id}")
    @IsUser
    public EmpruntResponseDto obtenirEmpruntParId(@PathVariable int id) {
        return empruntService.obtenirEmpruntsParId(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Emprunt introuvable"));
    }

    @GetMapping("/liste/actif")
    @IsUser
    public List<EmpruntResponseDto> actifs(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirEmpruntSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/liste/demande")
    @IsUser
    public List<EmpruntResponseDto> demandes(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirDemandesEmpruntSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/liste/recent")
    @IsUser
    public List<EmpruntResponseDto> recent(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirEmpruntsRecentsSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/liste/retard")
    @IsUser
    public List<EmpruntResponseDto> retard(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirListeEmpruntEnRetardSelonRole(obtenirUtilisateur(user));
    }

    @GetMapping("/historique")
    @IsUser
    public List<EmpruntResponseDto> historique(@AuthenticationPrincipal UtilisateurDetails user) {
        return empruntService.obtenirHistoriqueEmpruntSelonRole(obtenirUtilisateur(user));
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @IsUser
    public EmpruntResponseDto creer(@RequestBody 
                                    @Valid EmpruntRequestDto dto,
                                    @AuthenticationPrincipal 
                                    UtilisateurDetails utilisateurDetails) {
        return empruntService.creerEmprunt(dto, obtenirUtilisateur(utilisateurDetails));
    }

    // =========================
    // UPDATE EMPRUNT
    // =========================

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsUser
    public void mettreAjour(@PathVariable int id, @RequestBody @Valid EmpruntRequestDto dto) {
        empruntService.modifierEmprunt(id, dto);
    }

    // =========================
    // CHANGEMENT DE STATUT SIMPLE (ACCEPTE, REFUSE, TERMINE...)
    // =========================

    @PatchMapping("/{id}/statut")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsAdmin
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsUser
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsAdmin
    public void accepterProlongation(@PathVariable int id) {
        empruntService.accepterProlongation(id);
    }

    // =========================
    // DEMANDE RETOUR ANTICIPÉ (utilisateur)
    // =========================

    @PatchMapping("/{id}/retour-anticipe/demande")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsUser
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsAdmin
    public void accepterRetourAnticipe(@PathVariable int id) {
        empruntService.accepterRetourAnticipe(id);
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsAdmin
    public void supprimerParId(@PathVariable int id) {
        empruntService.supprimerEmprunt(id);
    }
}
