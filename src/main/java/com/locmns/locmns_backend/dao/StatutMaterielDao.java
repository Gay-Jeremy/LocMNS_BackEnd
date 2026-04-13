package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.StatutMateriel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutMaterielDao extends JpaRepository<StatutMateriel, Integer> {
}
