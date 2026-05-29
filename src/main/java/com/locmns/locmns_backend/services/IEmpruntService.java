package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface IEmpruntService {

    Optional<EmpruntResponseDto> obtenirEmpruntsParId(Integer id);
    List<EmpruntResponseDto> obtenirTousLesEmprunts();
    List<EmpruntResponseDto> obtenirEmpruntsActifs();
    List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirDemandesEmprunt();
    List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirEmpruntsRecents();
    List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirListeEmpruntEnRetard();
    List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirHistoriqueEmprunt();
    List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur);
    EmpruntResponseDto creerEmprunt(EmpruntDto empruntDto);
    void modifierEmprunt(Integer id, EmpruntDto empruntDto);
    void supprimerEmprunt(Integer id);
    void modifierStatutEmpruntFromDto(int empruntId, StatutDto statutDto);

}