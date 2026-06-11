package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.DroitEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.DroitEmpruntResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.DroitEmpruntMapper;
import com.locmns.locmns_backend.model.DroitEmprunt;
import com.locmns.locmns_backend.repository.CategorieModeleRepository;
import com.locmns.locmns_backend.repository.CategorieUtilisateurRepository;
import com.locmns.locmns_backend.repository.DroitEmpruntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DroitEmpruntService implements IDroitEmpruntService {

    private final DroitEmpruntRepository droitEmpruntRepository;
    private final CategorieUtilisateurRepository categorieUtilisateurRepository;
    private final CategorieModeleRepository categorieModeleRepository;
    private final DroitEmpruntMapper droitEmpruntMapper;

    @Override
    public List<DroitEmpruntResponseDto> obtenirTousLesDroitsEmprunt() {
        return droitEmpruntMapper.toDtoList(droitEmpruntRepository.findAll());
    }

    @Override
    public List<DroitEmpruntResponseDto> obtenirDroitsParCategorieUtilisateur(Integer categorieUtilisateurId) {
        return droitEmpruntMapper.toDtoList(droitEmpruntRepository.findByCategorieUtilisateurId(categorieUtilisateurId));
    }

    @Transactional
    @Override
    public DroitEmpruntResponseDto creerDroitEmprunt(DroitEmpruntRequestDto dto) {
        verifierAssociations(dto.categorieUtilisateurId(), dto.categorieModeleId());
        DroitEmprunt droitEmprunt = droitEmpruntMapper.toEntity(dto);
        return droitEmpruntMapper.toDto(droitEmpruntRepository.save(droitEmprunt));
    }

    @Transactional
    @Override
    public void supprimerDroitEmprunt(Integer categorieUtilisateurId, Integer categorieModeleId) {
        DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele cle =
                new DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele(categorieUtilisateurId, categorieModeleId);
        if (!droitEmpruntRepository.existsById(cle)) {
            throw new RessourceIntrouvableException("Droit emprunt introuvable");
        }
        droitEmpruntRepository.deleteById(cle);
    }

    private void verifierAssociations(Integer categorieUtilisateurId, Integer categorieModeleId) {
        if (!categorieUtilisateurRepository.existsById(categorieUtilisateurId)) {
            throw new RessourceIntrouvableException("Categorie utilisateur introuvable : " + categorieUtilisateurId);
        }
        if (!categorieModeleRepository.existsById(categorieModeleId)) {
            throw new RessourceIntrouvableException("Categorie modele introuvable : " + categorieModeleId);
        }
    }
}
