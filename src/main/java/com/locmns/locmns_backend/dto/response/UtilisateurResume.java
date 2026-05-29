package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Utilisateur;

public record UtilisateurResume(Integer id, String nom, String prenom) {

    public static UtilisateurResume depuisEntite(Utilisateur utilisateur) {
        if (utilisateur == null) return null;
        return new UtilisateurResume(utilisateur.getId(), utilisateur.getNom(), utilisateur.getPrenom());
    }
}
