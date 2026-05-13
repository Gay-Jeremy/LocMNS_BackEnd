package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.ModeleDto;
import com.locmns.locmns_backend.exception.MetierException;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.CategorieModele;
import com.locmns.locmns_backend.model.Modele;
import com.locmns.locmns_backend.repository.CategorieModeleDao;
import com.locmns.locmns_backend.repository.ModeleDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeleService {

    private final ModeleDao modeleDao;
    private final CategorieModeleDao categorieModeleDao;

    public List<Modele> obtenirTousLesModeles() {
        return modeleDao.findAll();
    }

    public List<Modele> obtenirModeleParCategorie(int categorieModeleId) {
        return modeleDao.findByCategorieModeleId(categorieModeleId);
    }

    @Transactional
    public Modele creerModele(ModeleDto dto) {

        // Vérification unicité libellé
        if (modeleDao.existsByLibelle(dto.libelle())) {
            throw new MetierException("Un modèle avec le libellé '" + dto.libelle() + "' existe déjà");
        }

        // Vérification existence catégorie
        CategorieModele categorieModele = categorieModeleDao.findById(dto.categorieModeleId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie introuvable : " + dto.categorieModeleId()));

        Modele modele = new Modele();
        modele.setLibelle(dto.libelle());
        modele.setCategorieModele(categorieModele);

        return modeleDao.save(modele);
    }
}
