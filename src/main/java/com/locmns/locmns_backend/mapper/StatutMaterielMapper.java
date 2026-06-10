package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.response.StatutMaterielResume;
import com.locmns.locmns_backend.model.StatutMateriel;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link StatutMateriel}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}, {@code code}.
 */
@Component
public class StatutMaterielMapper implements Mapper<StatutMateriel, Void, StatutMaterielResume> {

    /** {@inheritDoc} */
    @Override
    public StatutMaterielResume toDto(StatutMateriel entity) {
        if (entity == null) return null;
        return new StatutMaterielResume(entity.getId(), entity.getLibelle(), entity.getCode());
    }

    /**
     * Non supporté : les statuts matériel sont des référentiels immuables.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public StatutMateriel toEntity(Void requestDto) {
        throw new UnsupportedOperationException("StatutMateriel ne supporte pas toEntity via ce mapper");
    }

    /**
     * Non supporté : les statuts matériel sont des référentiels immuables.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(Void requestDto, StatutMateriel entity) {
        throw new UnsupportedOperationException("StatutMateriel ne supporte pas update via ce mapper");
    }
}
