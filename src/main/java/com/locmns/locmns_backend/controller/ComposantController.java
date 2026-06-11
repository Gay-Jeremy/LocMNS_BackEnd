package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.ComposantRequestDto;
import com.locmns.locmns_backend.dto.response.ComposantResponseDto;
import com.locmns.locmns_backend.services.IComposantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/composants")
@RequiredArgsConstructor
public class ComposantController {

    private final IComposantService composantService;

    @GetMapping("/liste")
    public List<ComposantResponseDto> obtenirTousLesComposants() {
        return composantService.obtenirTousLesComposants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComposantResponseDto> obtenirComposantParId(@PathVariable int id) {
        return composantService.obtenirComposantParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ComposantResponseDto> creerComposant(@RequestBody @Valid ComposantRequestDto dto) {
        return new ResponseEntity<>(composantService.creerComposant(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerComposant(@PathVariable int id) {
        composantService.supprimerComposant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierComposant(
            @PathVariable int id,
            @RequestBody @Valid ComposantRequestDto dto) {
        composantService.modifierComposant(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
