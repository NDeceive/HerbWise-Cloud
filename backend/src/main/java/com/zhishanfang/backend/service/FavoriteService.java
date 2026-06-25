package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.FavoriteRequest;
import com.zhishanfang.backend.dto.FavoriteResponse;
import com.zhishanfang.backend.model.Favorite;
import com.zhishanfang.backend.repository.ArticleRepository;
import com.zhishanfang.backend.repository.FavoriteRepository;
import com.zhishanfang.backend.repository.RecipeRepository;
import com.zhishanfang.backend.repository.RecommendationResultRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final ArticleRepository articleRepository;
    private final RecommendationResultRepository recommendationResultRepository;
    private final ResponseMapper mapper;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            RecipeRepository recipeRepository,
            ArticleRepository articleRepository,
            RecommendationResultRepository recommendationResultRepository,
            ResponseMapper mapper
    ) {
        this.favoriteRepository = favoriteRepository;
        this.recipeRepository = recipeRepository;
        this.articleRepository = articleRepository;
        this.recommendationResultRepository = recommendationResultRepository;
        this.mapper = mapper;
    }

    public List<FavoriteResponse> list(Long userId) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public FavoriteResponse add(Long userId, FavoriteRequest request) {
        Favorite favorite = favoriteRepository
                .findByUserIdAndTargetTypeAndTargetId(userId, request.targetType(), request.targetId())
                .orElseGet(() -> {
                    Favorite created = new Favorite();
                    created.setUserId(userId);
                    created.setTargetType(request.targetType());
                    created.setTargetId(request.targetId());
                    return favoriteRepository.save(created);
                });
        return toResponse(favorite);
    }

    public void remove(Long id) {
        if (!favoriteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found");
        }
        favoriteRepository.deleteById(id);
    }

    private FavoriteResponse toResponse(Favorite favorite) {
        String targetType = favorite.getTargetType();
        Long targetId = favorite.getTargetId();
        if ("article".equals(targetType)) {
            return articleRepository.findById(targetId)
                    .map(article -> mapper.toFavorite(favorite, article.getTitle(), article.getSummary()))
                    .orElseGet(() -> mapper.toFavorite(favorite, "养生资讯", "该资讯暂不可用"));
        }
        if ("recommendation".equals(targetType)) {
            return recommendationResultRepository.findById(targetId)
                    .map(result -> mapper.toFavorite(favorite, result.getRecipeName(), result.getReason()))
                    .orElseGet(() -> mapper.toFavorite(favorite, "AI 药膳方案", "该方案暂不可用"));
        }
        return recipeRepository.findById(targetId)
                .map(recipe -> mapper.toFavorite(favorite, recipe.getName(), recipe.getSummary()))
                .orElseGet(() -> mapper.toFavorite(favorite, "药膳收藏", "该药膳暂不可用"));
    }
}
