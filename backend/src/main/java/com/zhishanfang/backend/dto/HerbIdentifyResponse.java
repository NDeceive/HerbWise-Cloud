package com.zhishanfang.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record HerbIdentifyResponse(
        Long id,
        String imageName,
        String herbName,
        String confidenceLevel,
        List<String> appearanceFeatures,
        List<String> effects,
        List<String> suitablePeople,
        List<String> warnings,
        List<String> recommendedRecipes,
        LocalDateTime createdAt
) {
}
