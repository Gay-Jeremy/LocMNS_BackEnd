package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.DocumentationRequestDto;
import com.locmns.locmns_backend.dto.response.DocumentationResponseDto;
import com.locmns.locmns_backend.services.IDocumentationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentations")
@RequiredArgsConstructor
public class DocumentationController {

    private final IDocumentationService documentationService;

    @GetMapping("/liste")
    public List<DocumentationResponseDto> obtenirToutesLesDocumentations() {
        return documentationService.obtenirToutesLesDocumentations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentationResponseDto> obtenirDocumentationParId(@PathVariable int id) {
        return documentationService.obtenirDocumentationParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<DocumentationResponseDto> creerDocumentation(
            @RequestBody @Valid DocumentationRequestDto dto) {
        return new ResponseEntity<>(documentationService.creerDocumentation(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerDocumentation(@PathVariable int id) {
        documentationService.supprimerDocumentation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierDocumentation(
            @PathVariable int id,
            @RequestBody @Valid DocumentationRequestDto dto) {
        documentationService.modifierDocumentation(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
