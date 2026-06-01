package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.*;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IEmpruntService {

    Optional<EmpruntResponseDto> obtenirEmpruntsParId(Integer id);
    List<EmpruntResponseDto> obtenirTousLesEmprunts();
    List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur);
    List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur);
    EmpruntResponseDto creerEmprunt(EmpruntRequestDto empruntRequestDto);
    void modifierEmprunt(Integer id, EmpruntRequestDto empruntRequestDto);

    @Transactional
    void demanderProlongation(int id, DemandeProlongationRequestDto dto);

    @Transactional
    void accepterProlongation(int id);

    @Transactional
    void demanderRetourAnticipe(int id, DemandeRetourAnticipeRequestDto dto);

    @Transactional
    void accepterRetourAnticipe(int id);

    void supprimerEmprunt(Integer id);

    @Transactional
    void modifierStatutEmprunt(int empruntId, StatutUpdateRequestDto statutUpdateRequestDto);

}