package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IEmpruntService {

    Optional<EmpruntResponseDto> obtenirEmpruntsParId(Integer id, Utilisateur connecte);
    List<EmpruntResponseDto> obtenirTousLesEmprunts(Utilisateur connecte);
    List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur);
    EmpruntResponseDto creerEmprunt(EmpruntRequestDto dto, Utilisateur connecte);
    void modifierEmprunt(Integer id, EmpruntRequestDto empruntRequestDto, Utilisateur connecte);

    @Transactional
    void demanderProlongation(int id, DemandeProlongationRequestDto dto, Utilisateur connecte);

    @Transactional
    void accepterProlongation(int id);

    @Transactional
    void demanderRetourAnticipe(int id, DemandeRetourAnticipeRequestDto dto, Utilisateur connecte);

    @Transactional
    void accepterRetourAnticipe(int id);

    void supprimerEmprunt(Integer id);

    @Transactional
    void modifierStatutEmprunt(int empruntId, EmpruntStatutRequestDto empruntStatutRequestDto);

}