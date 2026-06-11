package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.ComposantRequestDto;
import com.locmns.locmns_backend.dto.response.ComposantResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.ComposantMapper;
import com.locmns.locmns_backend.model.CategorieModele;
import com.locmns.locmns_backend.model.Composant;
import com.locmns.locmns_backend.repository.CategorieModeleRepository;
import com.locmns.locmns_backend.repository.ComposantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComposantService implements IComposantService {

    private final ComposantRepository composantRepository;
    private final CategorieModeleRepository categorieModeleRepository;
    private final ComposantMapper composantMapper;

    @Override
    public List<ComposantResponseDto> obtenirTousLesComposants() {
        return composantMapper.toDtoList(composantRepository.findAll());
    }

    @Override
    public Optional<ComposantResponseDto> obtenirComposantParId(Integer id) {
        return composantRepository.findById(id).map(composantMapper::toDto);
    }

    @Transactional
    @Override
    public ComposantResponseDto creerComposant(ComposantRequestDto dto) {
        Composant composant = composantMapper.toEntity(dto);
        composant.setCategoriesModeles(resoudreCategoriesModeles(dto.categorieModeleIds()));
        return composantMapper.toDto(composantRepository.save(composant));
    }

    @Transactional
    @Override
    public void modifierComposant(Integer id, ComposantRequestDto dto) {
        Composant composant = composantRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Composant introuvable : " + id));
        composantMapper.update(dto, composant);
        composant.setCategoriesModeles(resoudreCategoriesModeles(dto.categorieModeleIds()));
        composantRepository.save(composant);
    }

    @Transactional
    @Override
    public void supprimerComposant(Integer id) {
        if (!composantRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Composant introuvable : " + id);
        }
        composantRepository.deleteById(id);
    }

    private List<CategorieModele> resoudreCategoriesModeles(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> idsUniques = ids.stream().distinct().toList();
        List<CategorieModele> categories = categorieModeleRepository.findAllById(idsUniques);
        if (categories.size() != idsUniques.size()) {
            throw new RessourceIntrouvableException("Une categorie modele est introuvable");
        }
        return categories;
    }
}
