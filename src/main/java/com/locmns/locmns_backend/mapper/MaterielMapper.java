package com.locmns.locmns_backend.mapper;

import com.locmns.locmns_backend.dto.request.MaterielRequestDto;
import com.locmns.locmns_backend.dto.response.MaterielResumeDto;
import com.locmns.locmns_backend.dto.response.MaterielResponseDto;
import com.locmns.locmns_backend.model.Materiel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour {@link Materiel}.
 *
 * <p>Mapper "composé" : délègue le mapping de {@code statutMateriel},
 * {@code modele} et {@code salle} à leurs mappers respectifs.
 *
 * <h3>Deux représentations de sortie</h3>
 * <ul>
 *   <li>{@link #toDto}         — {@link MaterielResumeDto} (sans salle) :
 *       résumé utilisé comme objet imbriqué dans {@code EmpruntResponseDto}</li>
 *   <li>{@link #toResponseDto} — {@link MaterielResponseDto} (avec salle) :
 *       représentation complète exposée par les endpoints {@code /materiels}</li>
 * </ul>
 *
 * <h3>Champs non gérés par le mapper</h3>
 * <p>Les associations JPA ({@code modele}, {@code statutMateriel}, {@code salle})
 * sont des clés étrangères : leur résolution depuis les repositories
 * est déléguée au service appelant.
 */
@Component
@RequiredArgsConstructor
public class MaterielMapper implements Mapper<Materiel, MaterielRequestDto, MaterielResumeDto> {

    private final StatutMaterielMapper statutMaterielMapper;
    private final ModeleMapper modeleMapper;
    private final SalleMapper salleMapper;

    /**
     * {@inheritDoc}
     *
     * <p>Produit un résumé sans salle : {@code id}, {@code numSerie},
     * {@code statutMateriel}, {@code modele}.
     * Utilisé comme objet imbriqué dans {@code EmpruntResponseDto}.
     */
    @Override
    public MaterielResumeDto toDto(Materiel entity) {
        if (entity == null) return null;
        return new MaterielResumeDto(
                entity.getId(),
                entity.getNumSerie(),
                statutMaterielMapper.toDto(entity.getStatutMateriel()),
                modeleMapper.toDto(entity.getModele())
        );
    }

    /**
     * Produit la représentation complète d'un matériel, incluant la salle de rangement.
     *
     * <p>Utilisé par les endpoints dédiés au matériel ({@code GET /materiels}).
     *
     * @param entity l'entité à convertir ; peut être {@code null}
     * @return le DTO complet, ou {@code null} si {@code entity} est {@code null}
     */
    public MaterielResponseDto toResponseDto(Materiel entity) {
        if (entity == null) return null;
        return new MaterielResponseDto(
                entity.getId(),
                entity.getNumSerie(),
                salleMapper.toDto(entity.getSalle()),
                statutMaterielMapper.toDto(entity.getStatutMateriel()),
                modeleMapper.toDto(entity.getModele())
        );
    }

    /**
     * {@inheritDoc}
     *
     * <p>Initialise uniquement {@code numSerie}.
     * Le service est responsable de résoudre et affecter :
     * <ul>
     *   <li>{@code modele}         via {@code modeleRepository}</li>
     *   <li>{@code statutMateriel} via {@code statutMaterielRepository} (optionnel)</li>
     *   <li>{@code salle}          via {@code salleRepository} (optionnel)</li>
     * </ul>
     */
    @Override
    public Materiel toEntity(MaterielRequestDto dto) {
        if (dto == null) return null;
        Materiel materiel = new Materiel();
        materiel.setNumSerie(dto.numSerie());
        // modele, statutMateriel, salle : résolution déléguée au service
        return materiel;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Met à jour uniquement {@code numSerie}.
     * Le service est responsable de la mise à jour des associations
     * {@code modele}, {@code statutMateriel} et {@code salle}.
     */
    @Override
    public void update(MaterielRequestDto dto, Materiel entity) {
        if (dto == null || entity == null) return;
        entity.setNumSerie(dto.numSerie());
        // modele, statutMateriel, salle : mise à jour déléguée au service
    }
}
