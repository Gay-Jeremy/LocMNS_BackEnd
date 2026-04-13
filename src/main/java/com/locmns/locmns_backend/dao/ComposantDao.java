package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.Composant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComposantDao extends JpaRepository<Composant, Integer> {
}
