package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.PromotionRequestDto;
import com.locmns.locmns_backend.dto.response.PromotionResumeDto;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.services.IPromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final IPromotionService promotionService;

    @GetMapping("/liste")
    @IsUser
    public List<PromotionResumeDto> obtenirToutesLesPromotions() {
        return promotionService.obtenirToutesLesPromotions();
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<PromotionResumeDto> obtenirPromotionParId(@PathVariable int id) {
        return promotionService.obtenirPromotionParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<PromotionResumeDto> creerPromotion(@RequestBody @Valid PromotionRequestDto dto) {
        return new ResponseEntity<>(promotionService.creerPromotion(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerPromotion(@PathVariable int id) {
        promotionService.supprimerPromotion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierPromotion(
            @PathVariable int id,
            @RequestBody @Valid PromotionRequestDto dto) {
        promotionService.modifierPromotion(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
