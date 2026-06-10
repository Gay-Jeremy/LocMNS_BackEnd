package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.response.CategorieModeleResume;
import com.locmns.locmns_backend.model.CategorieModele;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link CategorieModele}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}.
 *
 * <p>Note : {@code CategorieModele} ne possède pas de DTO de requête dédié
 * (elle est gérée via ses propres endpoints). Les méthodes {@code toEntity}
 * et {@code update} lèvent donc {@link UnsupportedOperationException}.
 */
@Component
public class CategorieModeleMapper implements Mapper<CategorieModele, Void, CategorieModeleResume> {

    /** {@inheritDoc} */
    @Override
    public CategorieModeleResume toDto(CategorieModele entity) {
        if (entity == null) return null;
        return new CategorieModeleResume(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code CategorieModele} est créée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public CategorieModele toEntity(Void requestDto) {
        throw new UnsupportedOperationException("CategorieModele ne supporte pas toEntity via ce mapper");
    }

    /**
     * Non supporté : {@code CategorieModele} est modifiée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(Void requestDto, CategorieModele entity) {
        throw new UnsupportedOperationException("CategorieModele ne supporte pas update via ce mapper");
    }
}
