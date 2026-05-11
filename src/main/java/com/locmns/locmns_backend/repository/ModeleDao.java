package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.Modele;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeleDao extends JpaRepository<Modele, Integer> {
}
