package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.ModeleRequestDto;
import com.locmns.locmns_backend.model.Modele;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IModeleService {
    List<Modele> obtenirTousLesModeles();

    List<Modele> obtenirModeleParCategorie(int categorieModeleId);

    @Transactional
    Modele creerModele(ModeleRequestDto dto);
}
