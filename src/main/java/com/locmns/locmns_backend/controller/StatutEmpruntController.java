package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.StatutEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.StatutEmpruntResumeDto;
import com.locmns.locmns_backend.services.IStatutEmpruntService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuts_emprunt")
@RequiredArgsConstructor
public class StatutEmpruntController {

    private final IStatutEmpruntService statutEmpruntService;

    @GetMapping("/liste")
    public List<StatutEmpruntResumeDto> obtenirTousLesStatutsEmprunt() {
        return statutEmpruntService.obtenirTousLesStatutsEmprunt();
    }

    @GetMapping("/liste/decision")
    public List<StatutEmpruntResumeDto> obtenirToutesLesDecisions() {
        return statutEmpruntService.obtenirToutesLesDecisions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutEmpruntResumeDto> obtenirStatutEmpruntParId(@PathVariable int id) {
        return statutEmpruntService.obtenirStatutEmpruntParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<StatutEmpruntResumeDto> creerStatutEmprunt(
            @RequestBody @Valid StatutEmpruntRequestDto dto) {
        return new ResponseEntity<>(statutEmpruntService.creerStatutEmprunt(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerStatutEmprunt(@PathVariable int id) {
        statutEmpruntService.supprimerStatutEmprunt(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierStatutEmprunt(
            @PathVariable int id,
            @RequestBody @Valid StatutEmpruntRequestDto dto) {
        statutEmpruntService.modifierStatutEmprunt(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
