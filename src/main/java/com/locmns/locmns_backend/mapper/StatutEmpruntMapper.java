package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.StatutEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.StatutEmpruntResumeDto;
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
public class StatutEmpruntMapper implements Mapper<StatutEmprunt, StatutEmpruntRequestDto, StatutEmpruntResumeDto> {

    /** {@inheritDoc} */
    @Override
    public StatutEmpruntResumeDto toDto(StatutEmprunt entity) {
        if (entity == null) return null;
        return new StatutEmpruntResumeDto(entity.getId(), entity.getLibelle(), entity.getCode());
    }

    /**
     * Non supporté : les statuts d'emprunt sont des référentiels immuables,
     * gérés via leurs propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public StatutEmprunt toEntity(StatutEmpruntRequestDto requestDto) {
        if (requestDto == null) return null;
        StatutEmprunt statutEmprunt = new StatutEmprunt();
        statutEmprunt.setLibelle(requestDto.libelle());
        statutEmprunt.setCode(requestDto.code());
        return statutEmprunt;
    }

    /**
     * Non supporté : les statuts d'emprunt sont des référentiels immuables.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(StatutEmpruntRequestDto requestDto, StatutEmprunt entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
