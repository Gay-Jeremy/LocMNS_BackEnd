package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Utilisateur;

public record UtilisateurResponseDto(
        Integer id,
        String nom,
        String prenom,
        String email,
        PromotionResumeDto promotion,
        CategorieUtilisateurResumeDto categorie
        ) {

    public static UtilisateurResponseDto depuisEntite(Utilisateur utilisateur) {
        return new UtilisateurResponseDto(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                PromotionResumeDto.depuisEntite(utilisateur.getPromotion()),
                CategorieUtilisateurResumeDto.depuisEntite(utilisateur.getCategorieUtilisateur())
        );
    }
}
