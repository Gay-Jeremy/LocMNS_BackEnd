package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.PromotionRequestDto;
import com.locmns.locmns_backend.dto.response.PromotionResumeDto;

import java.util.List;
import java.util.Optional;

public interface IPromotionService {
    List<PromotionResumeDto> obtenirToutesLesPromotions();
    Optional<PromotionResumeDto> obtenirPromotionParId(Integer id);
    PromotionResumeDto creerPromotion(PromotionRequestDto dto);
    void modifierPromotion(Integer id, PromotionRequestDto dto);
    void supprimerPromotion(Integer id);
}
