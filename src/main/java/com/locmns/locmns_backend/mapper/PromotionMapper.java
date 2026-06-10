package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.response.PromotionResume;
import com.locmns.locmns_backend.model.Promotion;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Promotion}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}.
 */
@Component
public class PromotionMapper implements Mapper<Promotion, Void, PromotionResume> {

    /** {@inheritDoc} */
    @Override
    public PromotionResume toDto(Promotion entity) {
        if (entity == null) return null;
        return new PromotionResume(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code Promotion} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public Promotion toEntity(Void requestDto) {
        throw new UnsupportedOperationException("Promotion ne supporte pas toEntity via ce mapper");
    }

    /**
     * Non supporté : {@code Promotion} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(Void requestDto, Promotion entity) {
        throw new UnsupportedOperationException("Promotion ne supporte pas update via ce mapper");
    }
}
