package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.HomeResponse;
import com.zhishanfang.backend.repository.ArticleRepository;
import com.zhishanfang.backend.repository.FavoriteRepository;
import com.zhishanfang.backend.repository.RecipeRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HomeService {
    private final RecipeRepository recipeRepository;
    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;
    private final ResponseMapper mapper;
    private final Long mockUserId;

    public HomeService(
            RecipeRepository recipeRepository,
            ArticleRepository articleRepository,
            FavoriteRepository favoriteRepository,
            ResponseMapper mapper,
            @Value("${app.mock-user-id:1}") Long mockUserId
    ) {
        this.recipeRepository = recipeRepository;
        this.articleRepository = articleRepository;
        this.favoriteRepository = favoriteRepository;
        this.mapper = mapper;
        this.mockUserId = mockUserId;
    }

    public HomeResponse home() {
        return new HomeResponse(
                recipeRepository.findTop6ByOrderByPopularityDesc().stream().limit(4).map(mapper::toRecipe).toList(),
                recipeRepository.findTop4BySeasonOrderByPopularityDesc("夏季").stream().map(mapper::toRecipe).toList(),
                articleRepository.findAllByOrderByCreatedAtDesc().stream().limit(4).map(mapper::toArticle).toList(),
                Map.of(
                        "medicated_food", (int) favoriteRepository.countByUserIdAndTargetType(mockUserId, "recipe"),
                        "soup", (int) favoriteRepository.countByUserIdAndTargetType(mockUserId, "soup"),
                        "tea", (int) favoriteRepository.countByUserIdAndTargetType(mockUserId, "tea")
                )
        );
    }
}
