package com.locmns.locmns_backend.mock;

import com.locmns.locmns_backend.dto.EmpruntDto;
import com.locmns.locmns_backend.dto.StatutDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import com.locmns.locmns_backend.services.IEmpruntService;

import java.util.List;
import java.util.Optional;

public class MockEmpruntService implements IEmpruntService {

    @Override
    public Optional<EmpruntResponseDto> obtenirEmpruntsParId(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<EmpruntResponseDto> obtenirTousLesEmprunts() {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsActifs() {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirDemandesEmprunt() {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsRecents() {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirListeEmpruntEnRetard() {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirHistoriqueEmprunt() {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public void supprimerEmprunt(Integer id) {}

    @Override
    public void modifierEmprunt(Integer id, EmpruntDto empruntDto) {}

    @Override
    public void modifierStatutEmpruntFromDto(int empruntId, StatutDto statutDto) {}

    @Override
    public EmpruntResponseDto creerEmprunt(EmpruntDto empruntDto) {
        return null;
    }
}