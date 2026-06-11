package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.ComposantRequestDto;
import com.locmns.locmns_backend.dto.response.ComposantResponseDto;
import com.locmns.locmns_backend.model.Composant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComposantMapper implements Mapper<Composant, ComposantRequestDto, ComposantResponseDto> {

    private final CategorieModeleMapper categorieModeleMapper;

    @Override
    public ComposantResponseDto toDto(Composant entity) {
        if (entity == null) return null;
        return new ComposantResponseDto(
                entity.getId(),
                entity.getLibelle(),
                entity.getCategoriesModeles() == null
                        ? java.util.List.of()
                        : entity.getCategoriesModeles().stream()
                        .map(categorieModeleMapper::toDto)
                        .toList()
        );
    }

    @Override
    public Composant toEntity(ComposantRequestDto requestDto) {
        if (requestDto == null) return null;
        Composant composant = new Composant();
        composant.setLibelle(requestDto.libelle());
        return composant;
    }

    @Override
    public void update(ComposantRequestDto requestDto, Composant entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
