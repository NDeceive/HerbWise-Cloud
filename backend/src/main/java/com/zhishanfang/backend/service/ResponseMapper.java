package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.ArticleResponse;
import com.zhishanfang.backend.dto.FavoriteResponse;
import com.zhishanfang.backend.dto.RecipeResponse;
import com.zhishanfang.backend.dto.RecommendationResponse;
import com.zhishanfang.backend.dto.UserResponse;
import com.zhishanfang.backend.model.Article;
import com.zhishanfang.backend.model.Favorite;
import com.zhishanfang.backend.model.Recipe;
import com.zhishanfang.backend.model.RecommendationResult;
import com.zhishanfang.backend.model.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {
    public UserResponse toUser(User user) {
        return new UserResponse(user.getId(), user.getPhone(), user.getNickname(), user.getAvatar());
    }

    public RecipeResponse toRecipe(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getType(),
                recipe.getCategory(),
                recipe.getImageUrl(),
                recipe.getSummary(),
                TextList.split(recipe.getTags()),
                TextList.split(recipe.getIngredients()),
                recipe.getSteps(),
                recipe.getEffects(),
                recipe.getSuitablePeople(),
                recipe.getContraindications(),
                recipe.getCookingTime(),
                recipe.getPopularity() == null ? 0 : recipe.getPopularity(),
                recipe.getSeason()
        );
    }

    public ArticleResponse toArticle(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getCategory(),
                article.getCover(),
                article.getSummary(),
                article.getContent(),
                article.getViews() == null ? 0 : article.getViews(),
                article.getCreatedAt()
        );
    }

    public FavoriteResponse toFavorite(Favorite favorite, String title, String summary) {
        return new FavoriteResponse(
                favorite.getId(),
                favorite.getTargetType(),
                favorite.getTargetId(),
                title,
                summary,
                favorite.getCreatedAt()
        );
    }

    public RecommendationResponse toRecommendation(
            RecommendationResult result,
            List<RecipeResponse> relatedRecipes,
            List<RecipeResponse> teaPairings
    ) {
        return new RecommendationResponse(
                result.getId(),
                result.getRecipeName(),
                result.getReason(),
                TextList.split(result.getRelatedRecipes()),
                TextList.split(result.getIngredients()),
                TextList.split(result.getSteps()),
                TextList.split(result.getWarnings()),
                result.getBodyAnalysis(),
                relatedRecipes,
                teaPairings,
                result.getCreatedAt()
        );
    }
}
