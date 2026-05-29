package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Promotion;

public record PromotionResume(Integer id, String libelle) {

    public static PromotionResume depuisEntite(Promotion p) {
        if (p == null) return null;
        return new PromotionResume(p.getId(), p.getLibelle());
    }
}
