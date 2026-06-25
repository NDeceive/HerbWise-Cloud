package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.HealthProfileRequest;
import com.zhishanfang.backend.dto.RecommendationResponse;

public interface AiRecommendationService {
    RecommendationResponse recommend(HealthProfileRequest request);

    RecommendationResponse findById(Long id);
}
