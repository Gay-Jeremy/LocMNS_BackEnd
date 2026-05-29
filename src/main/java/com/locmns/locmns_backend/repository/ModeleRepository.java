package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.Modele;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeleRepository extends JpaRepository<Modele, Integer> {

    List<Modele> findByCategorieModeleId(int categorieModeleId);
    boolean existsByLibelle(String libelle);
}
