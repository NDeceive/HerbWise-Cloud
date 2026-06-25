package com.zhishanfang.backend.dto;

import java.util.List;
import java.util.Map;

public record HomeResponse(
        List<RecipeResponse> recommended,
        List<RecipeResponse> seasonal,
        List<ArticleResponse> articles,
        Map<String, Integer> favoriteSummary
) {
}
