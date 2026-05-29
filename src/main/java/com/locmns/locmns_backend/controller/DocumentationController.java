package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.repository.DocumentationRepository;
import com.locmns.locmns_backend.model.Documentation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/documentations")
@RequiredArgsConstructor
public class DocumentationController {

    private final DocumentationRepository documentationRepository;

    @GetMapping("/liste")
    public List<Documentation> obtenirToutesLesDocumentations() {

        return documentationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documentation> obtenirDocumentationParId(@PathVariable int id) {
        Optional<Documentation> optionalDocumentation = documentationRepository.findById(id);
        if(optionalDocumentation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDocumentation.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public Documentation creerDocumentation(@RequestBody @Valid Documentation documentationToInsert) {
        documentationToInsert.setId(null);
        documentationRepository.save(documentationToInsert);
        return documentationToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerDocumentation(@PathVariable int id) {
        Optional<Documentation> optionalDocumentation = documentationRepository.findById(id);
        if (optionalDocumentation.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        documentationRepository.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierDocumentation(@PathVariable int id,
                                                      @RequestBody @Valid Documentation documentationToUpdate) {

        documentationToUpdate.setId(id);

        Optional<Documentation> optionalDocumentation = documentationRepository.findById(id);
        if (optionalDocumentation.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        documentationRepository.save(documentationToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
