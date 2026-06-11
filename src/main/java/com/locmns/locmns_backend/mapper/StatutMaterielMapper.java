package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.StatutMaterielRequestDto;
import com.locmns.locmns_backend.dto.response.StatutMaterielResumeDto;
import com.locmns.locmns_backend.model.StatutMateriel;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link StatutMateriel}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}, {@code code}.
 */
@Component
public class StatutMaterielMapper implements Mapper<StatutMateriel, StatutMaterielRequestDto, StatutMaterielResumeDto> {

    /** {@inheritDoc} */
    @Override
    public StatutMaterielResumeDto toDto(StatutMateriel entity) {
        if (entity == null) return null;
        return new StatutMaterielResumeDto(entity.getId(), entity.getLibelle(), entity.getCode());
    }

    /**
     * Non supporté : les statuts matériel sont des référentiels immuables.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public StatutMateriel toEntity(StatutMaterielRequestDto requestDto) {
        if (requestDto == null) return null;
        StatutMateriel statutMateriel = new StatutMateriel();
        statutMateriel.setLibelle(requestDto.libelle());
        statutMateriel.setCode(requestDto.code());
        return statutMateriel;
    }

    /**
     * Non supporté : les statuts matériel sont des référentiels immuables.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(StatutMaterielRequestDto requestDto, StatutMateriel entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
