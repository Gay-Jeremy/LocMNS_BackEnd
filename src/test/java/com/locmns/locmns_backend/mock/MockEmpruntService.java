package com.locmns.locmns_backend.mock;

import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.model.Emprunt;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.services.IEmpruntService;

import java.util.List;
import java.util.Optional;

public class MockEmpruntService implements IEmpruntService {
    @Override
    public Optional<Emprunt> obtenirEmpruntsParId(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Emprunt> obtenirTousLesEmprunts() {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirEmpruntsActifs() {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirDemandesEmprunt() {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirEmpruntsRecents() {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirListeEmpruntEnRetard() {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirHistoriqueEmprunt() {
        return List.of();
    }

    @Override
    public List<Emprunt> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public void supprimerEmprunt(Integer id) {

    }

    @Override
    public void modifierEmprunt(Integer id, EmpruntDto empruntDto) {

    }

    @Override
    public void modifierStatutEmpruntFromDto(int empruntId, StatutDto statutDto) {

    }

    @Override
    public Emprunt creerEmprunt(EmpruntDto empruntDto) {
        return null;
    }
}
