package com.zhishanfang.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RecommendationResponse(
        Long id,
        String recipeName,
        String reason,
        List<String> tags,
        List<String> ingredients,
        List<String> steps,
        List<String> warnings,
        String bodyAnalysis,
        List<RecipeResponse> relatedRecipes,
        List<RecipeResponse> teaPairings,
        LocalDateTime createdAt
) {
}
