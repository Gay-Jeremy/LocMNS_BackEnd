package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.StatutMateriel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutMaterielRepository extends JpaRepository<StatutMateriel, Integer> {
}
