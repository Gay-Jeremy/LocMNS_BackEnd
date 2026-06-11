package com.locmns.locmns_backend.dto.response;

import com.locmns.locmns_backend.model.Salle;

public record SalleResumeDto(Integer id, String libelle) {

    public static SalleResumeDto depuisEntite(Salle s) {
        if (s == null) return null;
        return new SalleResumeDto(s.getId(), s.getLibelle());
    }

}