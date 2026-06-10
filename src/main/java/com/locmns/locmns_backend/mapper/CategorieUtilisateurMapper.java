package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.response.CategorieUtilisateurResume;
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
public class CategorieUtilisateurMapper implements Mapper<CategorieUtilisateur, Void, CategorieUtilisateurResume> {

    /** {@inheritDoc} */
    @Override
    public CategorieUtilisateurResume toDto(CategorieUtilisateur entity) {
        if (entity == null) return null;
        return new CategorieUtilisateurResume(entity.getId(), entity.getLibelle());
    }

    /**
     * Non supporté : {@code CategorieUtilisateur} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public CategorieUtilisateur toEntity(Void requestDto) {
        throw new UnsupportedOperationException("CategorieUtilisateur ne supporte pas toEntity via ce mapper");
    }

    /**
     * Non supporté : {@code CategorieUtilisateur} est gérée via ses propres endpoints.
     *
     * @throws UnsupportedOperationException toujours
     */
    @Override
    public void update(Void requestDto, CategorieUtilisateur entity) {
        throw new UnsupportedOperationException("CategorieUtilisateur ne supporte pas update via ce mapper");
    }
}
