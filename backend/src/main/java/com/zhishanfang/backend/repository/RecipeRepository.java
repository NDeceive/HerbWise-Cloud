package com.zhishanfang.backend.repository;

import com.zhishanfang.backend.model.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByTypeOrderByPopularityDesc(String type);

    List<Recipe> findByCategoryOrderByPopularityDesc(String category);

    List<Recipe> findByTypeAndCategoryOrderByPopularityDesc(String type, String category);

    List<Recipe> findTop6ByOrderByPopularityDesc();

    List<Recipe> findTop4BySeasonOrderByPopularityDesc(String season);
}
