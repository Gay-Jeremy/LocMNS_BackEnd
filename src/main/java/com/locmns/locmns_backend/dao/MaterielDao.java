package com.locmns.locmns_backend.dao;


import com.locmns.locmns_backend.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterielDao extends JpaRepository<Materiel, Integer> {

    @Query("FROM Materiel m WHERE m.emprunts IS EMPTY OR NOT EXISTS (SELECT e FROM m.emprunts e WHERE e.dateRetourReel IS NULL)")
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
        AND e.statutEmprunt.id != 2
        )
    """)
    List<Materiel> retourneListeMaterielDisponiblePourPeriodeParIdCategorieModele(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("id") int id);

    List<Materiel> id(Integer id);
}
