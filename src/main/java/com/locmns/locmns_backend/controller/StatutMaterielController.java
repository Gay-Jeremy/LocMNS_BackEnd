package com.locmns.locmns_backend.controller;

import com.locmns.locmns_backend.dto.request.StatutMaterielRequestDto;
import com.locmns.locmns_backend.dto.response.StatutMaterielResumeDto;
import com.locmns.locmns_backend.security.IsAdmin;
import com.locmns.locmns_backend.security.IsUser;
import com.locmns.locmns_backend.services.IStatutMaterielService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuts_materiel")
@RequiredArgsConstructor
public class StatutMaterielController {

    private final IStatutMaterielService statutMaterielService;

    @GetMapping("/liste")
    @IsUser
    public List<StatutMaterielResumeDto> obtenirTousLesStatutsMateriel() {
        return statutMaterielService.obtenirTousLesStatutsMateriel();
    }

    @GetMapping("/{id}")
    @IsUser
    public ResponseEntity<StatutMaterielResumeDto> obtenirStatutMaterielParId(@PathVariable int id) {
        return statutMaterielService.obtenirStatutMaterielParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<StatutMaterielResumeDto> creerStatutMateriel(
            @RequestBody @Valid StatutMaterielRequestDto dto) {
        return new ResponseEntity<>(statutMaterielService.creerStatutMateriel(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> supprimerStatutMateriel(@PathVariable int id) {
        statutMaterielService.supprimerStatutMateriel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> modifierStatutMateriel(
            @PathVariable int id,
            @RequestBody @Valid StatutMaterielRequestDto dto) {
        statutMaterielService.modifierStatutMateriel(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
