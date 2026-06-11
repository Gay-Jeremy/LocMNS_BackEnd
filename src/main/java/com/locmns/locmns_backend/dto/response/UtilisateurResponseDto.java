package com.locmns.locmns_backend.dto.response;

public record UtilisateurResponseDto(
        Integer id,
        String nom,
        String prenom,
        String email,
        PromotionResumeDto promotion,
        CategorieUtilisateurResumeDto categorie
        ) {

}
