package com.locmns.locmns_backend.mapper;

import java.util.List;

/**
 * Contrat générique de mapping bidirectionnel entre une entité JPA et son DTO.
 *
 * <p>Chaque implémentation couvre trois opérations fondamentales :
 * <ol>
 *   <li><b>toDto</b>     — conversion entité → DTO de réponse (lecture)</li>
 *   <li><b>toEntity</b>  — construction d'une nouvelle entité depuis un DTO de requête (création)</li>
 *   <li><b>update</b>    — mise à jour d'une entité existante depuis un DTO de requête (modification)</li>
 * </ol>
 *
 * <p>Les méthodes {@code toDto} et {@code toEntity} retournent {@code null}
 * si le paramètre d'entrée est {@code null}, afin de propager naturellement
 * les associations optionnelles sans lever de {@link NullPointerException}.
 *
 * <p>La méthode par défaut {@code toDtoList} évite de dupliquer le pattern
 * {@code stream().map().toList()} dans chaque service.
 *
 * @param <E> type de l'entité JPA
 * @param <R> type du DTO de requête (création / modification)
 * @param <D> type du DTO de réponse
 */
public interface Mapper<E, R, D> {

    /**
     * Convertit une entité JPA en DTO de réponse.
     *
     * @param entity l'entité à convertir ; peut être {@code null}
     * @return le DTO correspondant, ou {@code null} si {@code entity} est {@code null}
     */
    D toDto(E entity);

    /**
     * Construit une nouvelle entité JPA depuis un DTO de requête.
     *
     * <p><b>Attention :</b> cette méthode ne résout pas les associations
     * (clés étrangères vers d'autres entités). C'est la responsabilité du service
     * de charger les entités liées depuis leurs repositories avant de les injecter.
     *
     * @param requestDto le DTO portant les données entrantes ; peut être {@code null}
     * @return une nouvelle entité initialisée avec les champs scalaires,
     *         ou {@code null} si {@code requestDto} est {@code null}
     */
    E toEntity(R requestDto);

    /**
     * Met à jour les champs scalaires d'une entité existante depuis un DTO de requête.
     *
     * <p>Seuls les champs "simples" (String, dates, nombres, etc.) sont mis à jour.
     * Les associations JPA ({@code @ManyToOne}, {@code @OneToMany}) restent
     * sous la responsabilité du service appelant.
     *
     * @param requestDto le DTO portant les nouvelles valeurs
     * @param entity     l'entité à mettre à jour (modifiée sur place)
     */
    void update(R requestDto, E entity);

    /**
     * Convertit une liste d'entités en liste de DTOs de réponse.
     *
     * <p>Retourne une liste vide si {@code entities} est {@code null}.
     *
     * @param entities la liste d'entités à convertir
     * @return liste immuable de DTOs
     */
    default List<D> toDtoList(List<E> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(this::toDto).toList();
    }
}
