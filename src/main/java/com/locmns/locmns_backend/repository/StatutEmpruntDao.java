package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.StatutEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatutEmpruntDao extends JpaRepository<StatutEmprunt, Integer> {

    @Query("FROM StatutEmprunt st WHERE st.code IN ('ACCEPTE', 'REFUSE' )")
    List<StatutEmprunt> retourneDecisionDemandeEmprunt();

    Optional<StatutEmprunt> findByCode(String code);






}



