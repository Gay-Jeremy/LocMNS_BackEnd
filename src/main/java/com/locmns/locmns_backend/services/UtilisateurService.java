package com.locmns.locmns_backend.services;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.exception.RessourceIntrouvableException;
import com.locmns.locmns_backend.model.*;
import com.locmns.locmns_backend.repository.CategorieUtilisateurDao;
import com.locmns.locmns_backend.repository.PromotionDao;
import com.locmns.locmns_backend.repository.UtilisateurDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements IUtilisateurService {

    private final PasswordEncoder encoder ;

    private final UtilisateurDao utilisateurDao;
    private final CategorieUtilisateurDao categorieUtilisateurDao;
    private final PromotionDao promotionDao;


    @Override
    public List<UtilisateurResponseDto> obtenirTousLesUtilisateurs() {
        return utilisateurDao.findAll().stream()
                .map(UtilisateurResponseDto::depuisEntite)
                .toList();
    }

    @Override
    public Optional<UtilisateurResponseDto> obtenirUtilisateurParId(Integer id) {
        return utilisateurDao.findById(id)
                .map(UtilisateurResponseDto::depuisEntite);
    }

    @Override
    public UtilisateurResponseDto creerUtilisateur(UtilisateurRequestDto dto) {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.nom());
        utilisateur.setPrenom(dto.prenom());
        utilisateur.setEmail(dto.email());
        utilisateur.setPassword(encoder.encode(dto.password()));

        CategorieUtilisateur categorieUtilisateur = categorieUtilisateurDao.findById(dto.categorieUtilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie utilisateur introuvable"));


        Promotion promotion = null;
        if (dto.promotionId() != null) {
            promotion = promotionDao.findById(dto.promotionId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Promotion introuvable"));
        }

        utilisateur.setCategorieUtilisateur(categorieUtilisateur);
        utilisateur.setPromotion(promotion);

        return UtilisateurResponseDto.depuisEntite(utilisateurDao.save(utilisateur));

    }

    @Override
    public void modifierUtilisateur(Integer id, UtilisateurRequestDto dto) {

        Utilisateur utilisateur = utilisateurDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Utilisateur introuvable"));

        CategorieUtilisateur categorieUtilisateur = categorieUtilisateurDao.findById(dto.categorieUtilisateurId())
                .orElseThrow(() -> new RessourceIntrouvableException("Catégorie utilisateur introuvable"));


        Promotion promotion = null;
        if (dto.promotionId() != null) {
            promotion = promotionDao.findById(dto.promotionId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Promotion introuvable"));
        }

        utilisateur.setNom(dto.nom());
        utilisateur.setPrenom(dto.prenom());
        utilisateur.setEmail(dto.email());
        utilisateur.setCategorieUtilisateur(categorieUtilisateur);
        utilisateur.setPromotion(promotion);

        utilisateurDao.save(utilisateur);
    }

    @Override
    public void supprimerUtilisateur(Integer id) {

        if (!utilisateurDao.existsById(id)) {
            throw new RessourceIntrouvableException("Utilisateur introuvable");
        }
        utilisateurDao.deleteById(id);

    }
}
