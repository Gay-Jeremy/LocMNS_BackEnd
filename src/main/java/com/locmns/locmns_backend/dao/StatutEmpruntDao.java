package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.constants.CodeStatutEmprunt;
import com.locmns.locmns_backend.model.StatutEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatutEmpruntDao extends JpaRepository<StatutEmprunt, Integer> {

    @Query("FROM StatutEmprunt st WHERE st.libelle IN ('Accepter', 'Refuser' )")
    List<StatutEmprunt> retourneDecisionEmprunt();

    Optional<StatutEmprunt> findByCode(CodeStatutEmprunt code);
}
