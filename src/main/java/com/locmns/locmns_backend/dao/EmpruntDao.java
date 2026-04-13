package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpruntDao extends JpaRepository<Emprunt, Integer> {

    @Query("SELECT e FROM Emprunt e JOIN e.statutEmprunt st WHERE st.libelle = 'Accepté'")
    List<Emprunt> retourneListeEmpruntActif();

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.libelle LIKE 'Demande%'")
    List<Emprunt> retourneListeDemande();

    @Query ("SELECT e FROM Emprunt e JOIN e.statutEmprunt st WHERE e.dateCreation > CURRENT_DATE AND st.libelle ='Accepté' ORDER BY e.dateCreation")
    List<Emprunt> retourneEmpruntRecent();

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.libelle LIKE 'Panne%'")
    List<Emprunt> retourneListePanne();
}