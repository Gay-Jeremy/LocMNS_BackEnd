package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.SalleRequestDto;
import com.locmns.locmns_backend.dto.response.SalleResumeDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.SalleMapper;
import com.locmns.locmns_backend.model.Salle;
import com.locmns.locmns_backend.repository.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalleService implements ISalleService {

    private final SalleRepository salleRepository;
    private final SalleMapper salleMapper;

    @Override
    public List<SalleResumeDto> obtenirToutesLesSalles() {
        return salleMapper.toDtoList(salleRepository.findAll());
    }

    @Override
    public Optional<SalleResumeDto> obtenirSalleParId(Integer id) {
        return salleRepository.findById(id).map(salleMapper::toDto);
    }

    @Transactional
    @Override
    public SalleResumeDto creerSalle(SalleRequestDto dto) {
        return salleMapper.toDto(salleRepository.save(salleMapper.toEntity(dto)));
    }

    @Transactional
    @Override
    public void modifierSalle(Integer id, SalleRequestDto dto) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Salle introuvable : " + id));
        salleMapper.update(dto, salle);
        salleRepository.save(salle);
    }

    @Transactional
    @Override
    public void supprimerSalle(Integer id) {
        if (!salleRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Salle introuvable : " + id);
        }
        salleRepository.deleteById(id);
    }
}
