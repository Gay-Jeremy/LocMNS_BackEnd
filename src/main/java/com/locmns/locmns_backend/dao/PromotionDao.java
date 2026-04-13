package com.locmns.locmns_backend.dao;

import com.locmns.locmns_backend.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionDao extends JpaRepository<Promotion, Integer> {
}
