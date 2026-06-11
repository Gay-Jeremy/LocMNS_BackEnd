package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Utilisateur;

public record UtilisateurResumeDto(Integer id, String nom, String prenom) {

    public static UtilisateurResumeDto depuisEntite(Utilisateur utilisateur) {
        if (utilisateur == null) return null;
        return new UtilisateurResumeDto(utilisateur.getId(), utilisateur.getNom(), utilisateur.getPrenom());
    }
}
