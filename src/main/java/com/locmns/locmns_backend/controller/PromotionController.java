package com.locmns.locmns_backend.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.dao.PromotionDao;
import com.locmns.locmns_backend.model.Promotion;
import com.locmns.locmns_backend.view.PromotionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/promotion")
public class PromotionController {

    @Autowired
    protected PromotionDao promotionDao;

    @GetMapping("/liste")
    @JsonView(PromotionView .class)
    public List<Promotion> getAll() {
        return promotionDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> get(@PathVariable int id) {
        Optional<Promotion> optionalPromotion = promotionDao.findById(id);
        if(optionalPromotion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalPromotion.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public Promotion create(@RequestBody Promotion promotionToInsert) {
        promotionToInsert.setId(null);
        promotionDao.save(promotionToInsert);
        return promotionToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<Promotion> optionalPromotion = promotionDao.findById(id);
        if (optionalPromotion.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        promotionDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id,
                                       @RequestBody Promotion promotionToUpdate) {

        promotionToUpdate.setId(id);

        Optional<Promotion> optionalPromotion = promotionDao.findById(id);
        if (optionalPromotion.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        promotionDao.save(promotionToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
