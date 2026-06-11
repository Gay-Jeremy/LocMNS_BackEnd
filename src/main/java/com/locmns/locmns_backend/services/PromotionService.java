package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.PromotionRequestDto;
import com.locmns.locmns_backend.dto.response.PromotionResumeDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.PromotionMapper;
import com.locmns.locmns_backend.model.Promotion;
import com.locmns.locmns_backend.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionService implements IPromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public List<PromotionResumeDto> obtenirToutesLesPromotions() {
        return promotionMapper.toDtoList(promotionRepository.findAll());
    }

    @Override
    public Optional<PromotionResumeDto> obtenirPromotionParId(Integer id) {
        return promotionRepository.findById(id).map(promotionMapper::toDto);
    }

    @Transactional
    @Override
    public PromotionResumeDto creerPromotion(PromotionRequestDto dto) {
        return promotionMapper.toDto(promotionRepository.save(promotionMapper.toEntity(dto)));
    }

    @Transactional
    @Override
    public void modifierPromotion(Integer id, PromotionRequestDto dto) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Promotion introuvable : " + id));
        promotionMapper.update(dto, promotion);
        promotionRepository.save(promotion);
    }

    @Transactional
    @Override
    public void supprimerPromotion(Integer id) {
        if (!promotionRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Promotion introuvable : " + id);
        }
        promotionRepository.deleteById(id);
    }
}
