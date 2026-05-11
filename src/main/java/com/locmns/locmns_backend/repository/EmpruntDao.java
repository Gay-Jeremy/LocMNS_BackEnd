package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntDao extends JpaRepository<Emprunt, Integer> {

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.code= :code ")
    List<Emprunt> retourneListeEmpruntActif(@Param("code") String code);

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.code IN ('DEMANDE_EN_COURS', 'DEMANDE_PROLONGATION', 'DEMANDE_RETOUR_ANTICIPE')")
    List<Emprunt> retourneListeDemandeEmprunt();

    @Query("SELECT e FROM Emprunt e WHERE e.dateCreation >= :dateDebut AND e.statutEmprunt.code = 'ACCEPTE' ORDER BY e.dateCreation DESC")
    List<Emprunt> retourneEmpruntRecent(@Param("dateDebut") LocalDate dateDebut);
;

    @Query("SELECT e FROM Emprunt e WHERE e.dateFin < CURRENT_DATE AND e.dateRetourReel IS NULL AND e.statutEmprunt.code NOT IN ('DEMANDE_EN_COURS', 'REFUSE')")
    List<Emprunt> retourneEmpruntEnRetard();

    @Query("SELECT e FROM Emprunt e WHERE e.dateFin < CURRENT_DATE OR e.statutEmprunt.code = 'REFUSE' ORDER BY e.dateFin ASC")
    List<Emprunt> retourneHistoriqueEmprunt();



}