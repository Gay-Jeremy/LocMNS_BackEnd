package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.model.Utilisateur;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IEmpruntService {
    Optional<Emprunt> obtenirEmpruntsParId(Integer id);

    List<Emprunt> obtenirTousLesEmprunts();

    List<Emprunt> obtenirEmpruntsActifs();

    List<Emprunt> obtenirEmpruntSelonRole(Utilisateur utilisateur);

    List<Emprunt> obtenirDemandesEmprunt();

    List<Emprunt> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur);

    List<Emprunt> obtenirEmpruntsRecents();

    List<Emprunt> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur);

    List<Emprunt> obtenirListeEmpruntEnRetard();

    List<Emprunt> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur);

    List<Emprunt> obtenirHistoriqueEmprunt();

    List<Emprunt> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur);

    void supprimerEmprunt(Integer id);

    @Transactional
    void modifierEmprunt(Integer id, EmpruntDto empruntDto);

    @Transactional
    void modifierStatutEmpruntFromDto(int empruntId, StatutDto statutDto);

    @Transactional
    Emprunt creerEmprunt(EmpruntDto empruntDto);
}
