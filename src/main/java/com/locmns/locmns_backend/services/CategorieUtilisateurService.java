package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.CategorieUtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieUtilisateurResumeDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.CategorieUtilisateurMapper;
import com.locmns.locmns_backend.model.CategorieUtilisateur;
import com.locmns.locmns_backend.repository.CategorieUtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorieUtilisateurService implements ICategorieUtilisateurService {

    private final CategorieUtilisateurRepository categorieUtilisateurRepository;
    private final CategorieUtilisateurMapper categorieUtilisateurMapper;

    @Override
    public List<CategorieUtilisateurResumeDto> obtenirToutesLesCategoriesUtilisateur() {
        return categorieUtilisateurMapper.toDtoList(categorieUtilisateurRepository.findAll());
    }

    @Override
    public Optional<CategorieUtilisateurResumeDto> obtenirCategorieUtilisateurParId(Integer id) {
        return categorieUtilisateurRepository.findById(id).map(categorieUtilisateurMapper::toDto);
    }

    @Transactional
    @Override
    public CategorieUtilisateurResumeDto creerCategorieUtilisateur(CategorieUtilisateurRequestDto dto) {
        return categorieUtilisateurMapper.toDto(categorieUtilisateurRepository.save(categorieUtilisateurMapper.toEntity(dto)));
    }

    @Transactional
    @Override
    public void modifierCategorieUtilisateur(Integer id, CategorieUtilisateurRequestDto dto) {
        CategorieUtilisateur categorieUtilisateur = categorieUtilisateurRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Categorie utilisateur introuvable : " + id));
        categorieUtilisateurMapper.update(dto, categorieUtilisateur);
        categorieUtilisateurRepository.save(categorieUtilisateur);
    }

    @Transactional
    @Override
    public void supprimerCategorieUtilisateur(Integer id) {
        if (!categorieUtilisateurRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Categorie utilisateur introuvable : " + id);
        }
        categorieUtilisateurRepository.deleteById(id);
    }
}
