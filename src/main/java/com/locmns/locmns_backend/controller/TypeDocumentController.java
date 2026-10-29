package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.TypeDocumentRequestDto;
import com.locmns.locmns_backend.dto.response.TypeDocumentResponseDto;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.services.ITypeDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types_document")
@RequiredArgsConstructor
public class TypeDocumentController {

    private final ITypeDocumentService typeDocumentService;

    @GetMapping("/liste")
    @IsUser
    public List<TypeDocumentResponseDto> obtenirTousLesTypesDocument() {
        return typeDocumentService.obtenirTousLesTypesDocument();
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<TypeDocumentResponseDto> obtenirTypeDocumentParId(@PathVariable int id) {
        return typeDocumentService.obtenirTypeDocumentParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<TypeDocumentResponseDto> creerTypeDocument(
            @RequestBody @Valid TypeDocumentRequestDto dto) {
        return new ResponseEntity<>(typeDocumentService.creerTypeDocument(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerTypeDocument(@PathVariable int id) {
        typeDocumentService.supprimerTypeDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierTypeDocument(
            @PathVariable int id,
            @RequestBody @Valid TypeDocumentRequestDto dto) {
        typeDocumentService.modifierTypeDocument(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
