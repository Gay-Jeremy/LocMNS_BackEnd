package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.repository.TypeDocumentRepository;
import com.locmns.locmns_backend.model.TypeDocument;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/types_document")
@RequiredArgsConstructor
public class TypeDocumentController {

    private final TypeDocumentRepository typeDocumentRepository;

    @GetMapping("/liste")
    public List<TypeDocument> obtenirTousLesTypesDocument() {
        return typeDocumentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDocument> obtenirTypeDocumentParId(@PathVariable int id) {
        Optional<TypeDocument> opt = typeDocumentRepository.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(opt.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<TypeDocument>  creerTypeDocument(
            @RequestBody @Valid TypeDocument typeDocumentToInsert) {

        typeDocumentRepository.save(typeDocumentToInsert);

        return new ResponseEntity<>(typeDocumentToInsert, HttpStatus.CREATED) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTypeDocument(@PathVariable int id) {
        Optional<TypeDocument> opt = typeDocumentRepository.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        typeDocumentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierTypeDocument(@PathVariable int id,
                                                     @RequestBody @Valid TypeDocument typeDocumentToUpdate) {
        typeDocumentToUpdate.setId(id);
        Optional<TypeDocument> opt = typeDocumentRepository.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        typeDocumentRepository.save(typeDocumentToUpdate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
