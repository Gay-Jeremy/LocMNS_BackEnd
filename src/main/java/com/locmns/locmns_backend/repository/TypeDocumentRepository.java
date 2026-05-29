package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// dao/TypeDocumentRepository.java
@Repository
public interface TypeDocumentRepository extends JpaRepository<TypeDocument, Integer> {}
