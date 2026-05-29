package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentationRepository extends JpaRepository<Documentation, Integer> {
}
