package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.ModeleRequestDto;
import com.locmns.locmns_backend.dto.response.ModeleResume;
import com.locmns.locmns_backend.model.Modele;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Modele}.
 *
 * <p>Mapper "composé" : délègue le mapping de la catégorie à {@link CategorieModeleMapper}.
 *
 * <h3>Responsabilités</h3>
 * <ul>
 *   <li>{@code toDto}     — produit un {@link ModeleResume} avec sa catégorie imbriquée</li>
 *   <li>{@code toEntity}  — initialise les champs scalaires ({@code libelle}) ;
 *       l'association {@code categorieModele} est résolue par le service</li>
 *   <li>{@code update}    — met à jour {@code libelle} uniquement ;
 *       l'association {@code categorieModele} est gérée par le service</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class ModeleMapper implements Mapper<Modele, ModeleRequestDto, ModeleResume> {

    private final CategorieModeleMapper categorieModeleMapper;

    /**
     * {@inheritDoc}
     *
     * <p>Délègue la conversion de {@code categorieModele} à {@link CategorieModeleMapper#toDto}.
     */
    @Override
    public ModeleResume toDto(Modele entity) {
        if (entity == null) return null;
        return new ModeleResume(
                entity.getId(),
                entity.getLibelle(),
                categorieModeleMapper.toDto(entity.getCategorieModele())
        );
    }

    /**
     * {@inheritDoc}
     *
     * <p>Seul {@code libelle} est initialisé.
     * Le service doit appeler {@link Modele#setCategorieModele} après résolution
     * de la catégorie depuis le repository.
     */
    @Override
    public Modele toEntity(ModeleRequestDto dto) {
        if (dto == null) return null;
        Modele modele = new Modele();
        modele.setLibelle(dto.libelle());
        // categorieModele : résolution de l'association déléguée au service
        return modele;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Met à jour {@code libelle} uniquement.
     * Le service est responsable de la mise à jour de {@code categorieModele}.
     */
    @Override
    public void update(ModeleRequestDto dto, Modele entity) {
        if (dto == null || entity == null) return;
        entity.setLibelle(dto.libelle());
        // categorieModele : mise à jour déléguée au service
    }
}
