package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.ModeleRequestDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.CategorieModele;
import com.locmns.locmns_backend.model.Modele;
import com.locmns.locmns_backend.repository.CategorieModeleRepository;
import com.locmns.locmns_backend.repository.ModeleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeleService implements IModeleService {

    private final ModeleRepository modeleRepository;
    private final CategorieModeleRepository categorieModeleRepository;

    @Override
    public List<Modele> obtenirTousLesModeles() {
        return modeleRepository.findAll();
    }

    @Override
    public List<Modele> obtenirModeleParCategorie(int categorieModeleId) {
        return modeleRepository.findByCategorieModeleId(categorieModeleId);
    }

    @Transactional
    @Override
    public Modele creerModele(ModeleRequestDto dto) {

        // Vérification unicité libellé
        if (modeleRepository.existsByLibelle(dto.libelle())) {
            throw new MetierException("Un modèle avec le libellé '" + dto.libelle() + "' existe déjà");
        }

        // Vérification existence catégorie
        CategorieModele categorieModele = categorieModeleRepository.findById(dto.categorieModeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie introuvable : " + dto.categorieModeleId()));

        Modele modele = new Modele();
        modele.setLibelle(dto.libelle());
        modele.setCategorieModele(categorieModele);

        return modeleRepository.save(modele);
    }
}
