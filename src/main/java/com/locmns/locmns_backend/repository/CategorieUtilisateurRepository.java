package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.CategorieUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieUtilisateurRepository extends JpaRepository<CategorieUtilisateur, Integer> {

}
