package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.response.SalleResume;
import com.locmns.locmns_backend.model.Salle;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Salle}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}.
 */
@Component
public class SalleMapper implements Mapper<Salle, Void, SalleResume> {

    /** {@inheritDoc} */
    @Override
    public SalleResume toDto(Salle entity) {
        if (entity == null) return null;
        return new SalleResume(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code Salle} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public Salle toEntity(Void requestDto) {
        throw new UnsupportedOperationException("Salle ne supporte pas toEntity via ce mapper");
    }

    /**
     * Non supporté : {@code Salle} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(Void requestDto, Salle entity) {
        throw new UnsupportedOperationException("Salle ne supporte pas update via ce mapper");
    }
}
