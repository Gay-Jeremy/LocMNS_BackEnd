package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalleDao extends JpaRepository<Salle, Integer> {
}
