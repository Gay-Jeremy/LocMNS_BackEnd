package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.ModeleRequestDto;
import com.locmns.locmns_backend.dto.response.ModeleResumeDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.ModeleMapper;
import com.locmns.locmns_backend.model.CategorieModele;
import com.locmns.locmns_backend.model.Modele;
import com.locmns.locmns_backend.repository.CategorieModeleRepository;
import com.locmns.locmns_backend.repository.ModeleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModeleService implements IModeleService {

    private final ModeleRepository modeleRepository;
    private final CategorieModeleRepository categorieModeleRepository;
    private final ModeleMapper modeleMapper;

    @Override
    public List<ModeleResumeDto> obtenirTousLesModeles() {
        return modeleMapper.toDtoList(modeleRepository.findAll());
    }

    @Override
    public List<ModeleResumeDto> obtenirModeleParCategorie(int categorieModeleId) {
        return modeleMapper.toDtoList(modeleRepository.findByCategorieModeleId(categorieModeleId));
    }

    @Override
    public Optional<ModeleResumeDto> obtenirModeleParId(Integer id) {
        return modeleRepository.findById(id).map(modeleMapper::toDto);
    }

    @Transactional
    @Override
    public ModeleResumeDto creerModele(ModeleRequestDto dto) {

        // Vérification unicité libellé
        if (modeleRepository.existsByLibelle(dto.libelle())) {
            throw new MetierException("Un modèle avec le libellé '" + dto.libelle() + "' existe déjà");
        }

        // Vérification existence catégorie
        CategorieModele categorieModele = categorieModeleRepository.findById(dto.categorieModeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie introuvable : " + dto.categorieModeleId()));

        Modele modele = modeleMapper.toEntity(dto);
        modele.setCategorieModele(categorieModele);

        return modeleMapper.toDto(modeleRepository.save(modele));
    }

    @Transactional
    @Override
    public void modifierModele(Integer id, ModeleRequestDto dto) {
        Modele modele = modeleRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Modele introuvable : " + id));
        CategorieModele categorieModele = categorieModeleRepository.findById(dto.categorieModeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Categorie introuvable : " + dto.categorieModeleId()));
        modeleMapper.update(dto, modele);
        modele.setCategorieModele(categorieModele);
        modeleRepository.save(modele);
    }

    @Transactional
    @Override
    public void supprimerModele(Integer id) {
        if (!modeleRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Modele introuvable : " + id);
        }
        modeleRepository.deleteById(id);
    }
}
