package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.DroitEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.DroitEmpruntResponseDto;

import java.util.List;

public interface IDroitEmpruntService {
    List<DroitEmpruntResponseDto> obtenirTousLesDroitsEmprunt();
    List<DroitEmpruntResponseDto> obtenirDroitsParCategorieUtilisateur(Integer categorieUtilisateurId);
    DroitEmpruntResponseDto creerDroitEmprunt(DroitEmpruntRequestDto dto);
    void supprimerDroitEmprunt(Integer categorieUtilisateurId, Integer categorieModeleId);
}
