package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentationDao extends JpaRepository<Documentation, Integer> {
}
