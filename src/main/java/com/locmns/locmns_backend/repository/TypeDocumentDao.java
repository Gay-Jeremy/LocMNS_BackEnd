package com.locmns.locmns_backend.repository;

import com.locmns.locmns_backend.model.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// dao/TypeDocumentDao.java
@Repository
public interface TypeDocumentDao extends JpaRepository<TypeDocument, Integer> {}
