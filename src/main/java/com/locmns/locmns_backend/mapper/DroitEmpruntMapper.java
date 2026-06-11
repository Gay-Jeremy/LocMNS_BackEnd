package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.DroitEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.CategorieModeleResumeDto;
import com.locmns.locmns_backend.dto.response.CategorieUtilisateurResumeDto;
import com.locmns.locmns_backend.dto.response.DroitEmpruntResponseDto;
import com.locmns.locmns_backend.model.DroitEmprunt;
import org.springframework.stereotype.Component;

@Component
public class DroitEmpruntMapper implements Mapper<DroitEmprunt, DroitEmpruntRequestDto, DroitEmpruntResponseDto> {

    @Override
    public DroitEmpruntResponseDto toDto(DroitEmprunt entity) {
        if (entity == null) return null;
        return new DroitEmpruntResponseDto(
                entity.getCategorieUtilisateurId(),
                entity.getCategorieModeleId(),
                entity.isPeutEmprunter(),
                CategorieUtilisateurResumeDto.depuisEntite(entity.getCategorieUtilisateur()),
                CategorieModeleResumeDto.depuisEntite(entity.getCategorieModele())
        );
    }

    @Override
    public DroitEmprunt toEntity(DroitEmpruntRequestDto requestDto) {
        if (requestDto == null) return null;
        DroitEmprunt droitEmprunt = new DroitEmprunt();
        droitEmprunt.setCategorieUtilisateurId(requestDto.categorieUtilisateurId());
        droitEmprunt.setCategorieModeleId(requestDto.categorieModeleId());
        droitEmprunt.setPeutEmprunter(requestDto.peutEmprunter());
        return droitEmprunt;
    }

    @Override
    public void update(DroitEmpruntRequestDto requestDto, DroitEmprunt entity) {
        if (requestDto == null || entity == null) return;
        entity.setPeutEmprunter(requestDto.peutEmprunter());
    }
}
