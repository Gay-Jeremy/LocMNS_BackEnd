package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.CategorieUtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieUtilisateurResumeDto;
import com.locmns.locmns_backend.model.CategorieUtilisateur;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link CategorieUtilisateur}.
 *
 * <p>Mapper "feuille" : ne dépend d'aucun autre mapper.
 * Les champs gérés : {@code id}, {@code libelle}.
 *
 * <p>Note : le champ {@code code} est exclu du résumé car il est réservé
 * à la logique de sécurité (ex. {@code Role.ADMIN}) et n'a pas vocation
 * à être exposé dans les réponses imbriquées.
 */
@Component
public class CategorieUtilisateurMapper implements Mapper<CategorieUtilisateur, CategorieUtilisateurRequestDto, CategorieUtilisateurResumeDto> {

    /** {@inheritDoc} */
    @Override
    public CategorieUtilisateurResumeDto toDto(CategorieUtilisateur entity) {
        if (entity == null) return null;
        return new CategorieUtilisateurResumeDto(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code CategorieUtilisateur} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public CategorieUtilisateur toEntity(CategorieUtilisateurRequestDto requestDto) {
        if (requestDto == null) return null;
        CategorieUtilisateur categorieUtilisateur = new CategorieUtilisateur();
        categorieUtilisateur.setLibelle(requestDto.libelle());
        categorieUtilisateur.setCode(requestDto.code());
        return categorieUtilisateur;
    }

    /**
     * Non supporté : {@code CategorieUtilisateur} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(CategorieUtilisateurRequestDto requestDto, CategorieUtilisateur entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
