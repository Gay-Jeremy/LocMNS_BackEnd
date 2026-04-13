package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurDao extends JpaRepository<Utilisateur, Integer> {
}
