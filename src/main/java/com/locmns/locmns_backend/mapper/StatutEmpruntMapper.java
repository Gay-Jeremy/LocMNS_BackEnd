package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.response.StatutEmpruntResume;
import com.locmns.locmns_backend.model.StatutEmprunt;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link StatutEmprunt}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}, {@code code}.
 *
 * <p>Le champ {@code code} est inclus dans le résumé car le frontend
 * l'utilise pour piloter l'affichage conditionnel des actions disponibles
 * (ex. bouton "Accepter" visible uniquement si {@code code = DEMANDE_EN_COURS}).
 */
@Component
public class StatutEmpruntMapper implements Mapper<StatutEmprunt, Void, StatutEmpruntResume> {

    /** {@inheritDoc} */
    @Override
    public StatutEmpruntResume toDto(StatutEmprunt entity) {
        if (entity == null) return null;
        return new StatutEmpruntResume(entity.getId(), entity.getLibelle(), entity.getCode());
    }

    /**
     * Non supporté : les statuts d'emprunt sont des référentiels immuables,
     * gérés via leurs propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public StatutEmprunt toEntity(Void requestDto) {
        throw new UnsupportedOperationException("StatutEmprunt ne supporte pas toEntity via ce mapper");
    }

    /**
     * Non supporté : les statuts d'emprunt sont des référentiels immuables.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(Void requestDto, StatutEmprunt entity) {
        throw new UnsupportedOperationException("StatutEmprunt ne supporte pas update via ce mapper");
    }
}
