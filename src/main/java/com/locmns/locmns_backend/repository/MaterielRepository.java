package com.locmns.locmns_backend.repository;


import com.locmns.locmns_backend.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterielRepository extends JpaRepository<Materiel, Integer> {

    @Query("""
    SELECT DISTINCT m FROM Materiel m
    JOIN m.emprunts e
    WHERE e.utilisateur.email = :email
    AND e.dateRetourReel IS NULL
    AND e.statutEmprunt.code NOT IN ('DEMANDE_EN_COURS', 'REFUSE')
    """)
    List<Materiel> retourneListeMaterielEmpruntsEnCoursParEmail(@Param("email") String email);

    @Query("FROM Materiel m WHERE NOT EXISTS (SELECT e FROM m.emprunts e WHERE e.dateRetourReel IS NULL)")
    List<Materiel> retourneListeMaterielDisponible();

    @Query("""
    SELECT m FROM Materiel m
    WHERE m.modele.categorieModele.id = :id
    AND m.statutMateriel.id IS NULL
    AND m.id NOT IN (
        SELECT e.materiel.id FROM Emprunt e
        WHERE e.dateRetourReel IS NULL
        AND e.dateDebut <= :dateFin
        AND e.dateFin >= :dateDebut
        AND e.statutEmprunt.code NOT IN ('REFUSE', 'TERMINE')
        )
    """)
    List<Materiel> retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("id") int id);

    @Query("""
    SELECT m FROM Materiel m
    WHERE m.id = :id
    AND m.id NOT IN (
        SELECT e.materiel.id FROM Emprunt e
        WHERE e.dateRetourReel IS NULL
        AND e.dateDebut <= :dateFin
        AND e.dateFin >= :dateDebut
        AND e.statutEmprunt.code NOT IN ('REFUSE', 'TERMINE')
        )
    """)
    List<Materiel> retourneDisponibiliteMaterielPourPeriodeParIdMateriel(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("id") int id);

    @Query("SELECT m FROM Materiel m WHERE m.statutMateriel.code IN ('PANNE', 'INCIDENT')")
    List<Materiel> retourneListeMaterielPanneSignales();

    @Query("SELECT m FROM Materiel m  JOIN m.emprunts e WHERE m.statutMateriel.code IN ('PANNE', 'INCIDENT') AND e.utilisateur.email = :email AND e.dateRetourReel IS NULL")
    List<Materiel> retourneListeMaterielPanneSignalesParEmail(@Param("email") String email);

    Optional<Materiel> findByNumSerie(String numSerie);
}
