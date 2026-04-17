package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.DroitEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroitEmpruntDao extends JpaRepository<DroitEmprunt, Integer> {

    List<DroitEmprunt> findByCategorieUtilisateurId(Integer id);
}
