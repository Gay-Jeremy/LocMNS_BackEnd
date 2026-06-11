package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.StatutMaterielRequestDto;
import com.locmns.locmns_backend.dto.response.StatutMaterielResumeDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.StatutMaterielMapper;
import com.locmns.locmns_backend.model.StatutMateriel;
import com.locmns.locmns_backend.repository.StatutMaterielRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatutMaterielService implements IStatutMaterielService {

    private final StatutMaterielRepository statutMaterielRepository;
    private final StatutMaterielMapper statutMaterielMapper;

    @Override
    public List<StatutMaterielResumeDto> obtenirTousLesStatutsMateriel() {
        return statutMaterielMapper.toDtoList(statutMaterielRepository.findAll());
    }

    @Override
    public Optional<StatutMaterielResumeDto> obtenirStatutMaterielParId(Integer id) {
        return statutMaterielRepository.findById(id).map(statutMaterielMapper::toDto);
    }

    @Transactional
    @Override
    public StatutMaterielResumeDto creerStatutMateriel(StatutMaterielRequestDto dto) {
        return statutMaterielMapper.toDto(statutMaterielRepository.save(statutMaterielMapper.toEntity(dto)));
    }

    @Transactional
    @Override
    public void modifierStatutMateriel(Integer id, StatutMaterielRequestDto dto) {
        StatutMateriel statutMateriel = statutMaterielRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Statut materiel introuvable : " + id));
        statutMaterielMapper.update(dto, statutMateriel);
        statutMaterielRepository.save(statutMateriel);
    }

    @Transactional
    @Override
    public void supprimerStatutMateriel(Integer id) {
        if (!statutMaterielRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Statut materiel introuvable : " + id);
        }
        statutMaterielRepository.deleteById(id);
    }
}
