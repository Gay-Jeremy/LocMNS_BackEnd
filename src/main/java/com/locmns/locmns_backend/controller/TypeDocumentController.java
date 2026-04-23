package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dao.TypeDocumentDao;
import com.locmns.locmns_backend.model.TypeDocument;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// controller/TypeDocumentController.java
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/types_document")
public class TypeDocumentController {

    @Autowired
    private TypeDocumentDao typeDocumentDao;

    @GetMapping("/liste")
    public List<TypeDocument> obtenirTousLesTypesDocument() {
        return typeDocumentDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDocument> obtenirTypeDocumentParId(@PathVariable int id) {
        Optional<TypeDocument> opt = typeDocumentDao.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(opt.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public TypeDocument creerTypeDocument(@RequestBody @Valid TypeDocument typeDocumentToInsert) {
        typeDocumentToInsert.setId(null);
        typeDocumentDao.save(typeDocumentToInsert);
        return typeDocumentToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTypeDocument(@PathVariable int id) {
        Optional<TypeDocument> opt = typeDocumentDao.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        typeDocumentDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierTypeDocument(@PathVariable int id,
                                                     @RequestBody @Valid TypeDocument typeDocumentToUpdate) {
        typeDocumentToUpdate.setId(id);
        Optional<TypeDocument> opt = typeDocumentDao.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        typeDocumentDao.save(typeDocumentToUpdate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
