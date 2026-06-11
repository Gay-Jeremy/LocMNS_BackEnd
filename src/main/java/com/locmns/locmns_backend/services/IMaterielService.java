package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.MaterielRequestDto;
import com.locmns.locmns_backend.dto.response.MaterielResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IMaterielService {
    Optional<MaterielResponseDto> obtenirMaterielParId(Integer id);

    List<MaterielResponseDto> obtenirTousLesMateriels();

    List<MaterielResponseDto> obtenirMaterielsSelonRole(Utilisateur utilisateur);

    List<MaterielResponseDto> obtenirMaterielsDisponibles();

    List<MaterielResponseDto> obtenirListeMaterielPanneSignalee();

    List<MaterielResponseDto> obtenirListeMaterielPanneSignaleeSelonRole(Utilisateur utilisateur);

    List<MaterielResponseDto> obtenirMaterielsDisponiblesPourPeriodeParIdCategorieModele(
            LocalDate dateDebut, LocalDate dateFin, int id);

    void verifierDisponibilitePourProlongation(int empruntId, LocalDate nouvelleDateFin);

    MaterielResponseDto creerMateriel(MaterielRequestDto materielRequestDto, Utilisateur utilisateur);

    @Transactional
    void modifierMateriel(Integer id, MaterielRequestDto materielRequestDto);

    void modifierStatutMateriel(Integer materielId, Integer statutId);

    void supprimerMateriel(Integer id);
}
