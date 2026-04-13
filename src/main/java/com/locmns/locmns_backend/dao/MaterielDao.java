package com.locmns.locmns_backend.dao;


import com.locmns.locmns_backend.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaterielDao extends JpaRepository<Materiel, Integer> {

    @Query("FROM Materiel m WHERE m.emprunts IS EMPTY OR NOT EXISTS (SELECT e FROM m.emprunts e WHERE e.dateRetourReel IS NULL)")
    List<Materiel> retourneListeMaterielDisponible();

    @Query("""
    SELECT m FROM Materiel m
    LEFT JOIN m.emprunts e
        ON e.dateRetourReel IS NULL
       AND e.dateDebut <= :dateFin
       AND e.dateFin >= :dateDebut
    WHERE e.id IS NULL
    """)

    List<Materiel> materielDisponiblePeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}
