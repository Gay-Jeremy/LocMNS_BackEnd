package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Utilisateur;

public record UtilisateurResponseDto(
        Integer id,
        String nom,
        String prenom,
        String email,
        PromotionResume promotion,
        CategorieUtilisateurResume categorie
        ) {

    public static UtilisateurResponseDto depuisEntite(Utilisateur utilisateur) {
        return new UtilisateurResponseDto(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                PromotionResume.depuisEntite(utilisateur.getPromotion()),
                CategorieUtilisateurResume.depuisEntite(utilisateur.getCategorieUtilisateur())
        );
    }
}
