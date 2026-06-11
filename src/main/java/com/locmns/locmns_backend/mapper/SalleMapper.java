package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.SalleRequestDto;
import com.locmns.locmns_backend.dto.response.SalleResumeDto;
import com.locmns.locmns_backend.model.Salle;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Salle}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}.
 */
@Component
public class SalleMapper implements Mapper<Salle, SalleRequestDto, SalleResumeDto> {

    /** {@inheritDoc} */
    @Override
    public SalleResumeDto toDto(Salle entity) {
        if (entity == null) return null;
        return new SalleResumeDto(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code Salle} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public Salle toEntity(SalleRequestDto requestDto) {
        if (requestDto == null) return null;
        Salle salle = new Salle();
        salle.setLibelle(requestDto.libelle());
        return salle;
    }

    /**
     * Non supporté : {@code Salle} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(SalleRequestDto requestDto, Salle entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
