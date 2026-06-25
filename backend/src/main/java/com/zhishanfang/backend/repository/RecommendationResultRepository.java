package com.zhishanfang.backend.repository;

import com.zhishanfang.backend.model.RecommendationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, Long> {
}
