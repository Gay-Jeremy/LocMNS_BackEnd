package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntDao extends JpaRepository<Emprunt, Integer> {

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.libelle = 'Accepté'")
    List<Emprunt> retourneListeEmpruntActif();

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.libelle LIKE 'Demande%'")
    List<Emprunt> retourneListeDemandeEmprunt();

    @Query("SELECT e FROM Emprunt e WHERE e.dateCreation >= :dateDebut AND e.statutEmprunt.libelle = 'Accepté' ORDER BY e.dateCreation DESC")
    List<Emprunt> retourneEmpruntRecent(@Param("dateDebut") LocalDate dateDebut);
;

    @Query("FROM Emprunt e WHERE e.dateFin < CURRENT_DATE AND e.dateRetourReel IS NULL AND e.statutEmprunt.libelle NOT IN ('Demande en cours', 'Refusé') ")
    List<Emprunt> retourneEmpruntEnRetard();

    @Query("FROM Emprunt e WHERE e.dateFin < CURRENT_DATE OR e.statutEmprunt.libelle ='Refusé'")
    List<Emprunt> retourneHistoriqueEmprunt();

}