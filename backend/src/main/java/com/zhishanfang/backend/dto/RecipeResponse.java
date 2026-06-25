package com.zhishanfang.backend.dto;

import java.util.List;

public record RecipeResponse(
        Long id,
        String name,
        String type,
        String category,
        String imageUrl,
        String summary,
        List<String> tags,
        List<String> ingredients,
        String steps,
        String effects,
        String suitablePeople,
        String contraindications,
        String cookingTime,
        int popularity,
        String season
) {
}
