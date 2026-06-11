package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.StatutEmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.StatutEmpruntResumeDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.StatutEmpruntMapper;
import com.locmns.locmns_backend.model.StatutEmprunt;
import com.locmns.locmns_backend.repository.StatutEmpruntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatutEmpruntService implements IStatutEmpruntService {

    private final StatutEmpruntRepository statutEmpruntRepository;
    private final StatutEmpruntMapper statutEmpruntMapper;

    @Override
    public List<StatutEmpruntResumeDto> obtenirTousLesStatutsEmprunt() {
        return statutEmpruntMapper.toDtoList(statutEmpruntRepository.findAll());
    }

    @Override
    public List<StatutEmpruntResumeDto> obtenirToutesLesDecisions() {
        return statutEmpruntMapper.toDtoList(statutEmpruntRepository.retourneDecisionDemandeEmprunt());
    }

    @Override
    public Optional<StatutEmpruntResumeDto> obtenirStatutEmpruntParId(Integer id) {
        return statutEmpruntRepository.findById(id).map(statutEmpruntMapper::toDto);
    }

    @Transactional
    @Override
    public StatutEmpruntResumeDto creerStatutEmprunt(StatutEmpruntRequestDto dto) {
        return statutEmpruntMapper.toDto(statutEmpruntRepository.save(statutEmpruntMapper.toEntity(dto)));
    }

    @Transactional
    @Override
    public void modifierStatutEmprunt(Integer id, StatutEmpruntRequestDto dto) {
        StatutEmprunt statutEmprunt = statutEmpruntRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Statut emprunt introuvable : " + id));
        statutEmpruntMapper.update(dto, statutEmprunt);
        statutEmpruntRepository.save(statutEmprunt);
    }

    @Transactional
    @Override
    public void supprimerStatutEmprunt(Integer id) {
        if (!statutEmpruntRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Statut emprunt introuvable : " + id);
        }
        statutEmpruntRepository.deleteById(id);
    }
}
