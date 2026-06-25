package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.RecipeResponse;
import com.zhishanfang.backend.dto.RecommendationResponse;
import com.zhishanfang.backend.model.HealthProfile;
import com.zhishanfang.backend.model.Recipe;
import com.zhishanfang.backend.model.RecommendationResult;
import com.zhishanfang.backend.repository.RecommendationResultRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MockAiService implements AiService {
    private final RecipeService recipeService;
    private final RecommendationResultRepository recommendationResultRepository;
    private final ResponseMapper mapper;

    public MockAiService(
            RecipeService recipeService,
            RecommendationResultRepository recommendationResultRepository,
            ResponseMapper mapper
    ) {
        this.recipeService = recipeService;
        this.recommendationResultRepository = recommendationResultRepository;
        this.mapper = mapper;
    }

    @Override
    public RecommendationResponse recommend(Long userId, HealthProfile profile) {
        Recipe recipe = selectRecipe(profile);
        RecommendationResult result = new RecommendationResult();
        result.setUserId(userId);
        result.setProfileId(profile.getId());
        result.setRecipeName(recipe.getName());
        result.setReason(reasonFor(recipe, profile));
        result.setIngredients(recipe.getIngredients());
        result.setSteps(TextList.join(stepsFor(recipe.getName())));
        result.setWarnings(TextList.join(warningsFor(recipe)));
        result.setBodyAnalysis(bodyAnalysisFor(recipe, profile));
        result.setRelatedRecipes(recipe.getTags());
        RecommendationResult saved = recommendationResultRepository.save(result);
        return toResponse(saved, recipe.getId());
    }

    @Override
    public RecommendationResponse findById(Long id) {
        RecommendationResult result = recommendationResultRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommendation result not found"));
        Recipe recipe = recipeService.findByNameOrFirst(result.getRecipeName());
        return toResponse(result, recipe.getId());
    }

    private Recipe selectRecipe(HealthProfile profile) {
        String markers = String.join("、",
                nullToBlank(profile.getConstitutionTypes()),
                nullToBlank(profile.getSymptoms()),
                nullToBlank(profile.getGoals())
        );
        if (markers.contains("气虚质") || markers.contains("疲劳乏力") || markers.contains("补气养血")) {
            return recipeService.findByNameOrFirst("当归黄芪乌鸡汤");
        }
        if (markers.contains("湿热质") || markers.contains("容易上火") || markers.contains("祛湿")) {
            return recipeService.findByNameOrFirst("茯苓山药粥");
        }
        if (markers.contains("失眠多梦") || markers.contains("安神") || markers.contains("改善睡眠")) {
            return recipeService.findByNameOrFirst("莲子百合银耳羹");
        }
        if (markers.contains("减脂塑形")) {
            return recipeService.findByNameOrFirst("冬瓜薏米汤");
        }
        return recipeService.findByNameOrFirst("当归黄芪乌鸡汤");
    }

    private RecommendationResponse toResponse(RecommendationResult result, Long recipeId) {
        List<RecipeResponse> related = recipeService.popularExcept(recipeId, 3);
        List<RecipeResponse> teas = recipeService.teaPairings(3);
        return mapper.toRecommendation(result, related, teas);
    }

    private String reasonFor(Recipe recipe, HealthProfile profile) {
        return "根据您填写的体质、症状与调理目标，系统优先匹配了「" + recipe.getName()
                + "」。该方案以" + recipe.getEffects() + "为主，兼顾日常食材可得性与温和调理。";
    }

    private String bodyAnalysisFor(Recipe recipe, HealthProfile profile) {
        String constitution = nullToBlank(profile.getConstitutionTypes());
        String symptoms = nullToBlank(profile.getSymptoms());
        String goals = nullToBlank(profile.getGoals());
        return "您的体质画像偏向「" + emptyFallback(constitution, "平和质或轻度偏颇体质")
                + "」，主要关注「" + emptyFallback(symptoms, "日常亚健康调理")
                + "」，调理目标为「" + emptyFallback(goals, "增强免疫力与规律养护")
                + "」。推荐方案适合体质：" + recipe.getSuitablePeople() + "。";
    }

    private List<String> stepsFor(String recipeName) {
        if (recipeName.contains("乌鸡汤")) {
            return List.of(
                    "乌鸡焯水去浮沫，当归、黄芪、党参、红枣、枸杞子清洗备用",
                    "将主料与药食材一同入砂锅，加清水约 1500ml",
                    "大火煮沸后转小火慢炖 2 小时，出锅前少许盐调味"
            );
        }
        if (recipeName.contains("山药粥")) {
            return List.of(
                    "茯苓、山药、薏米提前浸泡，粳米淘洗干净",
                    "所有食材入锅，加足量清水小火熬煮至米粒开花",
                    "口感可按需加少量盐或红枣调和，避免过甜"
            );
        }
        if (recipeName.contains("银耳羹")) {
            return List.of(
                    "银耳泡发撕小朵，莲子去芯，百合洗净",
                    "先煮银耳至出胶，再加入莲子、百合与少量冰糖",
                    "小火慢炖至软糯，睡前 2 小时温热食用更佳"
            );
        }
        return List.of(
                "食材洗净后按耐煮程度依次入锅",
                "加清水慢炖或熬煮，保持清淡少油",
                "根据个人口味少量调味，建议温热食用"
        );
    }

    private List<String> warningsFor(Recipe recipe) {
        return List.of(
                "本平台建议仅供参考，不能替代专业医疗诊断",
                recipe.getContraindications(),
                "如存在明确过敏食材、孕期、慢性病急性发作期，请先咨询专业医师",
                "忌口建议：少食辛辣油腻、生冷寒凉与个人过敏食材"
        );
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }

    private String emptyFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
