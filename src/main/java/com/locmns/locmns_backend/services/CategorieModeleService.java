package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.CategorieModeleRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieModeleResumeDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.CategorieModeleMapper;
import com.locmns.locmns_backend.model.CategorieModele;
import com.locmns.locmns_backend.repository.CategorieModeleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategorieModeleService implements ICategorieModeleService {

    private final CategorieModeleRepository categorieModeleRepository;
    private final CategorieModeleMapper categorieModeleMapper;

    @Override
    public List<CategorieModeleResumeDto> obtenirToutesLesCategoriesModele() {
        return categorieModeleMapper.toDtoList(categorieModeleRepository.findAll());
    }

    @Override
    public Optional<CategorieModeleResumeDto> obtenirCategorieModeleParId(Integer id) {
        return categorieModeleRepository.findById(id).map(categorieModeleMapper::toDto);
    }

    @Transactional
    @Override
    public CategorieModeleResumeDto creerCategorieModele(CategorieModeleRequestDto dto) {
        return categorieModeleMapper.toDto(categorieModeleRepository.save(categorieModeleMapper.toEntity(dto)));
    }

    @Transactional
    @Override
    public void modifierCategorieModele(Integer id, CategorieModeleRequestDto dto) {
        CategorieModele categorieModele = categorieModeleRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Categorie modele introuvable : " + id));
        categorieModeleMapper.update(dto, categorieModele);
        categorieModeleRepository.save(categorieModele);
    }

    @Transactional
    @Override
    public void supprimerCategorieModele(Integer id) {
        if (!categorieModeleRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Categorie modele introuvable : " + id);
        }
        categorieModeleRepository.deleteById(id);
    }
}
