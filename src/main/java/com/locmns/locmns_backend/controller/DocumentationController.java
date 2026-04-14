package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dao.DocumentationDao;
import com.locmns.locmns_backend.model.Documentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/documentation")
public class DocumentationController {

    @Autowired
    protected DocumentationDao documentationDao;

    @GetMapping("/liste")
    public List<Documentation> getAll() {

        return documentationDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documentation> get(@PathVariable int id) {
        Optional<Documentation> optionalDocumentation = documentationDao.findById(id);
        if(optionalDocumentation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDocumentation.get(),HttpStatus.OK);
    }

    @PostMapping("")
    public Documentation create(@RequestBody Documentation documentationToInsert) {
        documentationToInsert.setId(null);
        documentationDao.save(documentationToInsert);
        return documentationToInsert;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<Documentation> optionalDocumentation = documentationDao.findById(id);
        if (optionalDocumentation.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }
        documentationDao.deleteById(id);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id,
                                       @RequestBody Documentation documentationToUpdate) {

        documentationToUpdate.setId(id);

        Optional<Documentation> optionalDocumentation = documentationDao.findById(id);
        if (optionalDocumentation.isEmpty()) {
            return new ResponseEntity<>((HttpStatus.NOT_FOUND));
        }

        documentationDao.save(documentationToUpdate);

        return new ResponseEntity<>((HttpStatus.NO_CONTENT));
    }

}
