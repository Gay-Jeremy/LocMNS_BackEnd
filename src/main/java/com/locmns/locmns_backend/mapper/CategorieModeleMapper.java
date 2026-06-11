package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.CategorieModeleRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieModeleResumeDto;
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
public class CategorieModeleMapper implements Mapper<CategorieModele, CategorieModeleRequestDto, CategorieModeleResumeDto> {

    /** {@inheritDoc} */
    @Override
    public CategorieModeleResumeDto toDto(CategorieModele entity) {
        if (entity == null) return null;
        return new CategorieModeleResumeDto(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code CategorieModele} est créée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public CategorieModele toEntity(CategorieModeleRequestDto requestDto) {
        if (requestDto == null) return null;
        CategorieModele categorieModele = new CategorieModele();
        categorieModele.setLibelle(requestDto.libelle());
        return categorieModele;
    }

    /**
     * Non supporté : {@code CategorieModele} est modifiée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(CategorieModeleRequestDto requestDto, CategorieModele entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
