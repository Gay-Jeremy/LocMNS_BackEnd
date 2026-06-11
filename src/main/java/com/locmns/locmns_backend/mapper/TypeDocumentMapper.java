package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.TypeDocumentRequestDto;
import com.locmns.locmns_backend.dto.response.TypeDocumentResponseDto;
import com.locmns.locmns_backend.model.TypeDocument;
import org.springframework.stereotype.Component;

@Component
public class TypeDocumentMapper implements Mapper<TypeDocument, TypeDocumentRequestDto, TypeDocumentResponseDto> {

    @Override
    public TypeDocumentResponseDto toDto(TypeDocument entity) {
        if (entity == null) return null;
        return new TypeDocumentResponseDto(entity.getId(), entity.getLibelle());
    }

    @Override
    public TypeDocument toEntity(TypeDocumentRequestDto requestDto) {
        if (requestDto == null) return null;
        TypeDocument typeDocument = new TypeDocument();
        typeDocument.setLibelle(requestDto.libelle());
        return typeDocument;
    }

    @Override
    public void update(TypeDocumentRequestDto requestDto, TypeDocument entity) {
        if (requestDto == null || entity == null) return;
        entity.setLibelle(requestDto.libelle());
    }
}
