package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.SalleRequestDto;
import com.locmns.locmns_backend.dto.response.SalleResumeDto;
import com.locmns.locmns_backend.services.ISalleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
@RequiredArgsConstructor
public class SalleController {

    private final ISalleService salleService;

    @GetMapping("/liste")
    public List<SalleResumeDto> obtenirToutesLesSalles() {
        return salleService.obtenirToutesLesSalles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleResumeDto> obtenirSalleparId(@PathVariable int id) {
        return salleService.obtenirSalleParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<SalleResumeDto> creerSalle(@RequestBody @Valid SalleRequestDto dto) {
        return new ResponseEntity<>(salleService.creerSalle(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerSalle(@PathVariable int id) {
        salleService.supprimerSalle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierSalle(
            @PathVariable int id,
            @RequestBody @Valid SalleRequestDto dto) {
        salleService.modifierSalle(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
