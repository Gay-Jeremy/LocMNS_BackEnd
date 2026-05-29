package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Salle;

public record SalleResume(Integer id, String libelle) {

    public static SalleResume depuisEntite(Salle s) {
        if (s == null) return null;
        return new SalleResume(s.getId(), s.getLibelle());
    }

}