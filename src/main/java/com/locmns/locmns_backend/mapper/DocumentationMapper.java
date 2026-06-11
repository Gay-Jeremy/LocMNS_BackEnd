package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.DocumentationRequestDto;
import com.locmns.locmns_backend.dto.response.DocumentationResponseDto;
import com.locmns.locmns_backend.model.Documentation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentationMapper implements Mapper<Documentation, DocumentationRequestDto, DocumentationResponseDto> {

    private final TypeDocumentMapper typeDocumentMapper;
    private final ModeleMapper modeleMapper;

    @Override
    public DocumentationResponseDto toDto(Documentation entity) {
        if (entity == null) return null;
        return new DocumentationResponseDto(
                entity.getId(),
                entity.getTitre(),
                entity.getUrl(),
                entity.getFormat(),
                typeDocumentMapper.toDto(entity.getTypeDocument()),
                entity.getModeles() == null
                        ? java.util.List.of()
                        : entity.getModeles().stream()
                        .map(modeleMapper::toDto)
                        .toList()
        );
    }

    @Override
    public Documentation toEntity(DocumentationRequestDto requestDto) {
        if (requestDto == null) return null;
        Documentation documentation = new Documentation();
        update(requestDto, documentation);
        return documentation;
    }

    @Override
    public void update(DocumentationRequestDto requestDto, Documentation entity) {
        if (requestDto == null || entity == null) return;
        entity.setTitre(requestDto.titre());
        entity.setUrl(requestDto.url());
        entity.setFormat(requestDto.format());
    }
}
