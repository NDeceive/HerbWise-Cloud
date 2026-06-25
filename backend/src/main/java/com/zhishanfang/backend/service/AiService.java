package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.RecommendationResponse;
import com.zhishanfang.backend.model.HealthProfile;

public interface AiService {
    RecommendationResponse recommend(Long userId, HealthProfile profile);

    RecommendationResponse findById(Long id);
}
