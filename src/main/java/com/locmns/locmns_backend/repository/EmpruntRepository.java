package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {

    @Query("SELECT e FROM Emprunt e WHERE e.utilisateur.email = :email ORDER BY e.dateCreation")
    List<Emprunt> retourneListeEmpruntParEmail(@Param("email") String email);

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.code NOT IN :codes AND e.dateFin >= CURRENT_DATE")
    List<Emprunt> retourneListeEmpruntActif(@Param("codes")  List<String> codes);

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.code NOT IN :codes AND e.dateFin >= CURRENT_DATE AND e.utilisateur.email = :email")
    List<Emprunt> retourneListeEmpruntActifParEmail(@Param("codes") List<String> codes, @Param("email") String email);

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.code IN :codes")
    List<Emprunt> retourneListeDemandeEmprunt(@Param("codes") List<String> codes);

    @Query("SELECT e FROM Emprunt e WHERE e.statutEmprunt.code IN :codes AND e.utilisateur.email = :email")
    List<Emprunt> retourneListeDemandeEmpruntParEmail(@Param("codes") List<String> codes,
                                                      @Param("email") String email);

    @Query("SELECT e FROM Emprunt e WHERE e.dateCreation >= :dateDebut AND e.statutEmprunt.code = 'ACCEPTE' ORDER BY e.dateCreation DESC")
    List<Emprunt> retourneEmpruntRecent(@Param("dateDebut") LocalDate dateDebut);

    @Query("SELECT e FROM Emprunt e WHERE e.dateCreation >= :dateDebut AND e.statutEmprunt.code = 'ACCEPTE' AND e.utilisateur.email = :email ORDER BY e.dateCreation DESC")
    List<Emprunt> retourneEmpruntRecentParEmail(@Param("dateDebut") LocalDate dateDebut,
                                                @Param("email") String email);

    @Query("SELECT e FROM Emprunt e WHERE e.dateFin < CURRENT_DATE AND e.dateRetourReel IS NULL AND e.statutEmprunt.code NOT IN ('DEMANDE_EN_COURS', 'REFUSE')")
    List<Emprunt> retourneEmpruntEnRetard();

    @Query("SELECT e FROM Emprunt e WHERE e.dateFin < CURRENT_DATE AND e.dateRetourReel IS NULL AND e.statutEmprunt.code NOT IN ('DEMANDE_EN_COURS', 'REFUSE') AND e.utilisateur.email = :email")
    List<Emprunt> retourneEmpruntEnRetardParEmail(@Param("email") String email);

    @Query("SELECT e FROM Emprunt e WHERE e.dateFin < CURRENT_DATE AND e.statutEmprunt.code NOT IN ('DEMANDE_EN_COURS') ORDER BY e.dateFin ASC")
    List<Emprunt> retourneHistoriqueEmprunt();

    @Query("SELECT e FROM Emprunt e WHERE e.utilisateur.email = :email AND (e.dateFin < CURRENT_DATE  AND e.statutEmprunt.code NOT IN ('DEMANDE_EN_COURS')) ORDER BY e.dateFin ASC")
    List<Emprunt> retourneHistoriqueEmpruntParEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Emprunt e WHERE e.materiel.id = :materielId AND (e.dateRetourReel IS NULL AND e.statutEmprunt.code NOT IN ('REFUSE'))")
    boolean existsEmpruntActifParMaterielId(@Param("materielId") int materielId);

    @Query("""
        SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Emprunt e
        WHERE e.materiel.id = :materielId
        AND e.id <> :empruntId
        AND e.dateRetourReel IS NULL
        AND e.dateDebut <= :dateFin
        AND e.dateFin >= :dateDebut
        AND e.statutEmprunt.code NOT IN ('REFUSE')
        """)
    boolean existsConflitEmpruntSurMaterielPourPeriode(
            @Param("materielId") int materielId,
            @Param("empruntId") int empruntId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    @Transactional
    void deleteByMaterielId(int materielId);

}