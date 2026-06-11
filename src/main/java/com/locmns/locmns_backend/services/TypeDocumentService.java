package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.TypeDocumentRequestDto;
import com.locmns.locmns_backend.dto.response.TypeDocumentResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.TypeDocumentMapper;
import com.locmns.locmns_backend.model.TypeDocument;
import com.locmns.locmns_backend.repository.TypeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeDocumentService implements ITypeDocumentService {

    private final TypeDocumentRepository typeDocumentRepository;
    private final TypeDocumentMapper typeDocumentMapper;

    @Override
    public List<TypeDocumentResponseDto> obtenirTousLesTypesDocument() {
        return typeDocumentMapper.toDtoList(typeDocumentRepository.findAll());
    }

    @Override
    public Optional<TypeDocumentResponseDto> obtenirTypeDocumentParId(Integer id) {
        return typeDocumentRepository.findById(id).map(typeDocumentMapper::toDto);
    }

    @Transactional
    @Override
    public TypeDocumentResponseDto creerTypeDocument(TypeDocumentRequestDto dto) {
        return typeDocumentMapper.toDto(typeDocumentRepository.save(typeDocumentMapper.toEntity(dto)));
    }

    @Transactional
    @Override
    public void modifierTypeDocument(Integer id, TypeDocumentRequestDto dto) {
        TypeDocument typeDocument = typeDocumentRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Type document introuvable : " + id));
        typeDocumentMapper.update(dto, typeDocument);
        typeDocumentRepository.save(typeDocument);
    }

    @Transactional
    @Override
    public void supprimerTypeDocument(Integer id) {
        if (!typeDocumentRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Type document introuvable : " + id);
        }
        typeDocumentRepository.deleteById(id);
    }
}
