package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.DroitEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroitEmpruntDao extends JpaRepository<DroitEmprunt, DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele> {

    List<DroitEmprunt> findByCategorieUtilisateurId(Integer id);
}
