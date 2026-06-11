package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.PromotionRequestDto;
import com.locmns.locmns_backend.dto.response.PromotionResumeDto;
import com.locmns.locmns_backend.model.Promotion;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Promotion}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}.
 */
@Component
public class PromotionMapper implements Mapper<Promotion, PromotionRequestDto, PromotionResumeDto> {

    /** {@inheritDoc} */
    @Override
    public PromotionResumeDto toDto(Promotion entity) {
        if (entity == null) return null;
        return new PromotionResumeDto(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code Promotion} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public Promotion toEntity(PromotionRequestDto requestDto) {
        if (requestDto == null) return null;
        Promotion promotion = new Promotion();
        promotion.setLibelle(requestDto.libelle());
        return promotion;
    }

    /**
     * Non supporté : {@code Promotion} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(PromotionRequestDto requestDto, Promotion entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
