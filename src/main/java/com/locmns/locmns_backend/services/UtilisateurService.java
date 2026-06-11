package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.mapper.UtilisateurMapper;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.repository.CategorieUtilisateurRepository;
import com.locmns.locmns_backend.repository.PromotionRepository;
import com.locmns.locmns_backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements IUtilisateurService {

    private final PasswordEncoder encoder ;

    private final UtilisateurRepository utilisateurRepository;
    private final CategorieUtilisateurRepository categorieUtilisateurRepository;
    private final PromotionRepository promotionRepository;
    private final UtilisateurMapper utilisateurMapper;


    @Override
    public List<UtilisateurResponseDto> obtenirTousLesUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurMapper::toResponseDto)
                .toList();
    }

    @Override
    public Optional<UtilisateurResponseDto> obtenirUtilisateurParId(Integer id) {
        return utilisateurRepository.findById(id)
                .map(utilisateurMapper::toResponseDto);
    }

    @Override
    public UtilisateurResponseDto creerUtilisateur(UtilisateurRequestDto dto) {

        Utilisateur utilisateur = utilisateurMapper.toEntity(dto);
        utilisateur.setPassword(encoder.encode(dto.password()));

        CategorieUtilisateur categorieUtilisateur = categorieUtilisateurRepository.findById(dto.categorieUtilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie utilisateur introuvable"));


        Promotion promotion = null;
        if (dto.promotionId() != null) {
            promotion = promotionRepository.findById(dto.promotionId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Promotion introuvable"));
        }

        utilisateur.setCategorieUtilisateur(categorieUtilisateur);
        utilisateur.setPromotion(promotion);

        return utilisateurMapper.toResponseDto(utilisateurRepository.save(utilisateur));

    }

    @Override
    public void modifierUtilisateur(Integer id, UtilisateurRequestDto dto) {

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Utilisateur introuvable"));

        CategorieUtilisateur categorieUtilisateur = categorieUtilisateurRepository.findById(dto.categorieUtilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie utilisateur introuvable"));


        Promotion promotion = null;
        if (dto.promotionId() != null) {
            promotion = promotionRepository.findById(dto.promotionId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Promotion introuvable"));
        }

        utilisateurMapper.update(dto, utilisateur);
        utilisateur.setCategorieUtilisateur(categorieUtilisateur);
        utilisateur.setPromotion(promotion);

        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void supprimerUtilisateur(Integer id) {

        if (!utilisateurRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Utilisateur introuvable");
        }
        utilisateurRepository.deleteById(id);

    }
}
