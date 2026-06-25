package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.RecipeResponse;
import com.zhishanfang.backend.model.Recipe;
import com.zhishanfang.backend.repository.RecipeRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final ResponseMapper mapper;

    public RecipeService(RecipeRepository recipeRepository, ResponseMapper mapper) {
        this.recipeRepository = recipeRepository;
        this.mapper = mapper;
    }

    public List<RecipeResponse> list(String type, String category) {
        List<Recipe> recipes;
        boolean hasType = type != null && !type.isBlank();
        boolean hasCategory = category != null && !category.isBlank();
        if (hasType && hasCategory) {
            recipes = recipeRepository.findByTypeAndCategoryOrderByPopularityDesc(type, category);
        } else if (hasType) {
            recipes = recipeRepository.findByTypeOrderByPopularityDesc(type);
        } else if (hasCategory) {
            recipes = recipeRepository.findByCategoryOrderByPopularityDesc(category);
        } else {
            recipes = recipeRepository.findAll(Sort.by(Sort.Direction.DESC, "popularity"));
        }
        return recipes.stream().map(mapper::toRecipe).toList();
    }

    public RecipeResponse detail(Long id) {
        return mapper.toRecipe(findEntity(id));
    }

    public Recipe findEntity(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
    }

    public Recipe findByNameOrFirst(String name) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getName().equals(name))
                .findFirst()
                .orElseGet(() -> recipeRepository.findTop6ByOrderByPopularityDesc().get(0));
    }

    public List<RecipeResponse> popularExcept(Long recipeId, int limit) {
        return recipeRepository.findTop6ByOrderByPopularityDesc().stream()
                .filter(recipe -> !recipe.getId().equals(recipeId))
                .limit(limit)
                .map(mapper::toRecipe)
                .toList();
    }

    public List<RecipeResponse> teaPairings(int limit) {
        return recipeRepository.findByTypeOrderByPopularityDesc("tea").stream()
                .limit(limit)
                .map(mapper::toRecipe)
                .toList();
    }
}
