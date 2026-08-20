package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.EmpruntRequestDto;
import com.locmns.locmns_backend.dto.response.EmpruntResponseDto;
import com.locmns.locmns_backend.model.Emprunt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Emprunt}.
 *
 * <p>Mapper "racine" de la chaîne de composition : orchestre le mapping
 * de tous les objets imbriqués en déléguant à leurs mappers respectifs.
 *
 * <h3>Arbre de délégation</h3>
 * <pre>
 * EmpruntMapper
 * ├── UtilisateurMapper   → UtilisateurResumeDto
 * ├── StatutEmpruntMapper → StatutEmpruntResumeDto
 * └── MaterielMapper
 *     ├── StatutMaterielMapper → StatutMaterielResumeDto
 *     └── ModeleMapper
 *         └── CategorieModeleMapper → CategorieModeleResumeDto
 * </pre>
 *
 * <h3>Champs non gérés par le mapper</h3>
 * <p>Les associations JPA ({@code utilisateur}, {@code materiel}, {@code statutEmprunt})
 * sont des clés étrangères dont la résolution depuis les repositories
 * est déléguée au service appelant.
 *
 * <p>{@code dateCreation} est gérée automatiquement par JPA ({@code @PrePersist})
 * et n'est donc jamais affectée dans {@code toEntity} ni dans {@code update}.
 */
@Component
@RequiredArgsConstructor
public class EmpruntMapper implements Mapper<Emprunt, EmpruntRequestDto, EmpruntResponseDto> {

    private final UtilisateurMapper utilisateurMapper;
    private final StatutEmpruntMapper statutEmpruntMapper;
    private final MaterielMapper materielMapper;

    /**
     * {@inheritDoc}
     *
     * <p>Produit un {@link EmpruntResponseDto} complet avec toutes les associations imbriquées.
     */
    @Override
    public EmpruntResponseDto toDto(Emprunt entity) {
        if (entity == null) return null;
        return new EmpruntResponseDto(
                entity.getId(),
                entity.getDateCreation(),
                entity.getDateDebut(),
                entity.getDateFin(),
                entity.getDateRetourReel(),
                entity.getDateProlongation(),
                entity.getDateRetourAnticipe(),
                entity.getNote(),
                utilisateurMapper.toDto(entity.getUtilisateur()),
                statutEmpruntMapper.toDto(entity.getStatutEmprunt()),
                materielMapper.toDto(entity.getMateriel())
        );
    }

    /**
     * {@inheritDoc}
     *
     * <p>Initialise les champs scalaires : {@code dateDebut}, {@code dateFin}, {@code note}.
     * Le service est responsable de résoudre et affecter :
     * <ul>
     *   <li>{@code utilisateur}    via {@code utilisateurRepository}</li>
     *   <li>{@code materiel}       via {@code materielRepository}</li>
     *   <li>{@code statutEmprunt}  statut initial {@code DEMANDE_EN_COURS}</li>
     * </ul>
     */
    @Override
    public Emprunt toEntity(EmpruntRequestDto dto) {
        if (dto == null) return null;
        Emprunt emprunt = new Emprunt();
        emprunt.setDateDebut(dto.dateDebut());
        emprunt.setDateFin(dto.dateFin());
        emprunt.setNote(
                dto.note() == null || dto.note().isBlank()
                        ? null
                        : dto.note()
        );
  
        return emprunt;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Met à jour les champs scalaires : {@code dateDebut}, {@code dateFin}, {@code note}.
     * Le service est responsable de la mise à jour des associations
     * {@code utilisateur}, {@code materiel} et {@code statutEmprunt}.
     *
     * <p>{@code dateCreation} n'est jamais modifiée après la persistance initiale.
     */
    @Override
    public void update(EmpruntRequestDto dto, Emprunt entity) {
        if (dto == null || entity == null) return;
        entity.setDateDebut(dto.dateDebut());
        entity.setDateFin(dto.dateFin());
        entity.setNote(dto.note());
        // utilisateur, materiel : mise à jour déléguée au service
        // dateCreation : immuable après création
    }
}
