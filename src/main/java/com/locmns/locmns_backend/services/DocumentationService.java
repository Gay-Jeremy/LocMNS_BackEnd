package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.DocumentationRequestDto;
import com.locmns.locmns_backend.dto.response.DocumentationResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.DocumentationMapper;
import com.locmns.locmns_backend.model.Documentation;
import com.locmns.locmns_backend.model.Modele;
import com.locmns.locmns_backend.model.TypeDocument;
import com.locmns.locmns_backend.repository.DocumentationRepository;
import com.locmns.locmns_backend.repository.ModeleRepository;
import com.locmns.locmns_backend.repository.TypeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentationService implements IDocumentationService {

    private final DocumentationRepository documentationRepository;
    private final TypeDocumentRepository typeDocumentRepository;
    private final ModeleRepository modeleRepository;
    private final DocumentationMapper documentationMapper;

    @Override
    public List<DocumentationResponseDto> obtenirToutesLesDocumentations() {
        return documentationMapper.toDtoList(documentationRepository.findAll());
    }

    @Override
    public Optional<DocumentationResponseDto> obtenirDocumentationParId(Integer id) {
        return documentationRepository.findById(id).map(documentationMapper::toDto);
    }

    @Transactional
    @Override
    public DocumentationResponseDto creerDocumentation(DocumentationRequestDto dto) {
        Documentation documentation = documentationMapper.toEntity(dto);
        appliquerAssociations(documentation, dto);
        return documentationMapper.toDto(documentationRepository.save(documentation));
    }

    @Transactional
    @Override
    public void modifierDocumentation(Integer id, DocumentationRequestDto dto) {
        Documentation documentation = documentationRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Documentation introuvable : " + id));
        documentationMapper.update(dto, documentation);
        appliquerAssociations(documentation, dto);
        documentationRepository.save(documentation);
    }

    @Transactional
    @Override
    public void supprimerDocumentation(Integer id) {
        if (!documentationRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Documentation introuvable : " + id);
        }
        documentationRepository.deleteById(id);
    }

    private void appliquerAssociations(Documentation documentation, DocumentationRequestDto dto) {
        TypeDocument typeDocument = typeDocumentRepository.findById(dto.typeDocumentId())
                .orElseThrow(() -> new RessourceIntrouvableException("Type document introuvable : " + dto.typeDocumentId()));
        documentation.setTypeDocument(typeDocument);
        documentation.setModeles(resoudreModeles(dto.modeleIds()));
    }

    private List<Modele> resoudreModeles(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> idsUniques = ids.stream().distinct().toList();
        List<Modele> modeles = modeleRepository.findAllById(idsUniques);
        if (modeles.size() != idsUniques.size()) {
            throw new RessourceIntrouvableException("Un modele est introuvable");
        }
        return modeles;
    }
}
