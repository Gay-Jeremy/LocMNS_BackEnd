package com.locmns.locmns_backend.mock;

import com.locmns.locmns_backend.dto.request.*;
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
    public List<EmpruntResponseDto> obtenirEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirDemandesEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirEmpruntsRecentsSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public List<EmpruntResponseDto> obtenirListeEmpruntEnRetardSelonRole(Utilisateur utilisateur) {
        return List.of();
    }


    @Override
    public List<EmpruntResponseDto> obtenirHistoriqueEmpruntSelonRole(Utilisateur utilisateur) {
        return List.of();
    }

    @Override
    public void supprimerEmprunt(Integer id) {}

    @Override
    public void modifierEmprunt(Integer id, EmpruntRequestDto empruntRequestDto) {}

    @Override
    public void demanderProlongation(int id, DemandeProlongationRequestDto dto) {

    }

    @Override
    public void accepterProlongation(int id) {

    }

    @Override
    public void demanderRetourAnticipe(int id, DemandeRetourAnticipeRequestDto dto) {

    }

    @Override
    public void accepterRetourAnticipe(int id) {

    }

    @Override
    public void modifierStatutEmprunt(int empruntId, StatutUpdateRequestDto statutUpdateRequestDto) {

    }

    @Override
    public EmpruntResponseDto creerEmprunt(EmpruntRequestDto empruntRequestDto) {
        return null;
    }
}