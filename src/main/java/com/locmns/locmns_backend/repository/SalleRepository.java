package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Integer> {
}
