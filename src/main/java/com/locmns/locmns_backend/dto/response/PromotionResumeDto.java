package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Promotion;

public record PromotionResumeDto(Integer id, String libelle) {

    public static PromotionResumeDto depuisEntite(Promotion p) {
        if (p == null) return null;
        return new PromotionResumeDto(p.getId(), p.getLibelle());
    }
}
