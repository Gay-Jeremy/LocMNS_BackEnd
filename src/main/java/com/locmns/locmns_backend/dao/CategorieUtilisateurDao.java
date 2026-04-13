package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.CategorieUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieUtilisateurDao extends JpaRepository<CategorieUtilisateur, Integer> {

}
