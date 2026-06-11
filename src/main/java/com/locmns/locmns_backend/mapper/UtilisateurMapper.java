package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResumeDto;
import com.locmns.locmns_backend.dto.response.UtilisateurResponseDto;
import com.locmns.locmns_backend.model.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Utilisateur}.
 *
 * <p>Mapper "composé" : délègue le mapping de {@code promotion} et
 * {@code categorieUtilisateur} à leurs mappers respectifs.
 *
 * <h3>Deux représentations de sortie</h3>
 * <ul>
 *   <li>{@link #toDto}         — {@link UtilisateurResumeDto} (id, nom, prénom) :
 *       résumé léger utilisé comme objet imbriqué dans d'autres DTOs, ex. {@code EmpruntResponseDto}</li>
 *   <li>{@link #toResponseDto} — {@link UtilisateurResponseDto} (tous les champs) :
 *       représentation complète exposée par les endpoints {@code /utilisateurs}</li>
 * </ul>
 *
 * <h3>Champs non gérés par le mapper</h3>
 * <ul>
 *   <li>{@code password} : jamais exposé en sortie ; encodé par le service en entrée</li>
 *   <li>{@code promotion}, {@code categorieUtilisateur} : associations résolues par le service</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class UtilisateurMapper implements Mapper<Utilisateur, UtilisateurRequestDto, UtilisateurResumeDto> {

    private final PromotionMapper promotionMapper;
    private final CategorieUtilisateurMapper categorieUtilisateurMapper;

    /**
     * {@inheritDoc}
     *
     * <p>Produit un résumé léger : {@code id}, {@code nom}, {@code prenom} uniquement.
     * Utilisé lorsqu'un utilisateur est référencé à l'intérieur d'un autre DTO
     * (ex. {@code EmpruntResponseDto}).
     */
    @Override
    public UtilisateurResumeDto toDto(Utilisateur entity) {
        if (entity == null) return null;
        return new UtilisateurResumeDto(entity.getId(), entity.getNom(), entity.getPrenom());
    }

    /**
     * Produit la représentation complète d'un utilisateur, incluant email,
     * promotion et catégorie.
     *
     * <p>Utilisé par les endpoints dédiés aux utilisateurs ({@code GET /utilisateurs}).
     *
     * @param entity l'entité à convertir ; peut être {@code null}
     * @return le DTO complet, ou {@code null} si {@code entity} est {@code null}
     */
    public UtilisateurResponseDto toResponseDto(Utilisateur entity) {
        if (entity == null) return null;
        return new UtilisateurResponseDto(
                entity.getId(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getEmail(),
                promotionMapper.toDto(entity.getPromotion()),
                categorieUtilisateurMapper.toDto(entity.getCategorieUtilisateur())
        );
    }

    /**
     * {@inheritDoc}
     *
     * <p>Initialise les champs scalaires : {@code nom}, {@code prenom}, {@code email}.
     * Le service est responsable de :
     * <ul>
     *   <li>encoder et affecter {@code password}</li>
     *   <li>résoudre et affecter {@code promotion} et {@code categorieUtilisateur}</li>
     * </ul>
     */
    @Override
    public Utilisateur toEntity(UtilisateurRequestDto dto) {
        if (dto == null) return null;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.nom());
        utilisateur.setPrenom(dto.prenom());
        utilisateur.setEmail(dto.email());
        // password : encodage délégué au service (PasswordEncoder)
        // promotion, categorieUtilisateur : résolution déléguée au service
        return utilisateur;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Met à jour les champs scalaires : {@code nom}, {@code prenom}, {@code email}.
     * Le service est responsable de la mise à jour de {@code promotion}
     * et {@code categorieUtilisateur}.
     *
     * <p>Le {@code password} n'est jamais modifié ici ; son changement
     * passe par un endpoint dédié.
     */
    @Override
    public void update(UtilisateurRequestDto dto, Utilisateur entity) {
        if (dto == null || entity == null) return;
        entity.setNom(dto.nom());
        entity.setPrenom(dto.prenom());
        entity.setEmail(dto.email());
        // password : non modifié ici, endpoint dédié
        // promotion, categorieUtilisateur : mise à jour déléguée au service
    }
}
