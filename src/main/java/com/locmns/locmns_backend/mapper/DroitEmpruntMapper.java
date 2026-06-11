package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.DroitEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.DroitEmpruntResponseDto;
import com.locmns.locmns_backend.model.DroitEmprunt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DroitEmpruntMapper implements Mapper<DroitEmprunt, DroitEmpruntRequestDto, DroitEmpruntResponseDto> {

    private final CategorieUtilisateurMapper categorieUtilisateurMapper;
    private final CategorieModeleMapper categorieModeleMapper;

    @Override
    public DroitEmpruntResponseDto toDto(DroitEmprunt entity) {
        if (entity == null) return null;
        return new DroitEmpruntResponseDto(
                entity.getCategorieUtilisateurId(),
                entity.getCategorieModeleId(),
                entity.isPeutEmprunter(),
                categorieUtilisateurMapper.toDto(entity.getCategorieUtilisateur()),
                categorieModeleMapper.toDto(entity.getCategorieModele())
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
