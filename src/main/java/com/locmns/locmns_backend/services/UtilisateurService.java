package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.constants.Role;
import com.locmns.locmns_backend.exception.MetierException;
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

    private final PasswordEncoder encoder ;
    
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

    /**
     * Inscription publique : identique à la création, mais interdit de s'attribuer
     * la catégorie administrateur. Seul un admin peut créer un autre admin
     * (via {@link #creerUtilisateur}, endpoint protégé).
     *
     * @throws MetierException si la catégorie demandée est une catégorie administrateur
     */
    @Override
    public UtilisateurResponseDto inscrireUtilisateur(UtilisateurRequestDto dto) {
        CategorieUtilisateur categorie = categorieUtilisateurRepository.findById(dto.categorieUtilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie utilisateur introuvable"));

        if (Role.ADMIN.name().equals(categorie.getCode())) {
            throw new MetierException("Inscription impossible avec cette catégorie d'utilisateur");
        }
        return creerUtilisateur(dto);
    }
}
