package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;

import java.util.List;
import java.util.Optional;

public interface IUtilisateurService {

    List<UtilisateurResponseDto> obtenirTousLesUtilisateurs();
    Optional<UtilisateurResponseDto> obtenirUtilisateurParId(Integer id);
    UtilisateurResponseDto creerUtilisateur(UtilisateurRequestDto dto);
    void modifierUtilisateur(Integer id, UtilisateurRequestDto dto);
    void supprimerUtilisateur(Integer id);

}