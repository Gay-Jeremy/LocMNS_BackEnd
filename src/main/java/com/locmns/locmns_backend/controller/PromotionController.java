package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.repository.PromotionRepository;
import com.locmns.locmns_backend.model.Promotion;
import com.locmns.locmns_backend.view.PromotionView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionRepository promotionRepository;

    @GetMapping("/liste")
    @JsonView(PromotionView .class)
    public List<Promotion> obtenirToutesLesPromotions() {
        return promotionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> obtenirPromotionParId(@PathVariable int id) {
        Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
        if(optionalPromotion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalPromotion.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Promotion>  creerPromotion(
            @RequestBody @Valid Promotion promotionToInsert) {

        promotionRepository.save(promotionToInsert);

        return new ResponseEntity<>(promotionToInsert, HttpStatus.CREATED) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPromotion(@PathVariable int id) {
        Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
        if (optionalPromotion.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        promotionRepository.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierPromotion(@PathVariable int id,
                                                  @RequestBody @Valid Promotion promotionToUpdate) {

        promotionToUpdate.setId(id);

        Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
        if (optionalPromotion.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        promotionRepository.save(promotionToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
