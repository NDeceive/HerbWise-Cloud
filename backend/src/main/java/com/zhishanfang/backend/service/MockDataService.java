package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.ArticleResponse;
import com.zhishanfang.backend.dto.FavoriteRequest;
import com.zhishanfang.backend.dto.FavoriteResponse;
import com.zhishanfang.backend.dto.HomeResponse;
import com.zhishanfang.backend.dto.RecipeResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class MockDataService {
    private final AtomicLong favoriteIds = new AtomicLong(20);
    private final CopyOnWriteArrayList<FavoriteResponse> favorites = new CopyOnWriteArrayList<>(
            List.of(
                    new FavoriteResponse(1L, "recipe", 1L, "当归黄芪乌鸡汤", "补气养血，健脾益气", LocalDateTime.now().minusDays(2)),
                    new FavoriteResponse(2L, "tea", 9L, "玫瑰红枣奶茶", "理气补血，温柔养颜", LocalDateTime.now().minusDays(1))
            )
    );

    public HomeResponse home() {
        return new HomeResponse(
                recipes(null, null).stream().limit(4).toList(),
                recipes(null, null).stream().skip(1).limit(3).toList(),
                articles(),
                Map.of("medicated_food", 12, "soup", 8, "tea", 6)
        );
    }

    public List<RecipeResponse> recipes(String type, String category) {
        return allRecipes().stream()
                .filter(recipe -> type == null || type.isBlank() || recipe.type().equals(type))
                .filter(recipe -> category == null || category.isBlank() || recipe.category().equals(category))
                .toList();
    }

    public RecipeResponse recipe(Long id) {
        return allRecipes().stream()
                .filter(recipe -> recipe.id().equals(id))
                .findFirst()
                .orElse(allRecipes().get(0));
    }

    public List<ArticleResponse> articles() {
        return List.of(
                new ArticleResponse(1L, "立夏养心正当时：中医教你顺时养生", "节气养生", "", "立夏后天气渐热，养心畅阳、清补养阴是关键。", "立夏养生重在清补与规律作息。", 1243, LocalDateTime.now().minusDays(4)),
                new ArticleResponse(2L, "湿气重的表现与调理方法", "调理方法", "", "如何判断体内湿气重，日常调理有妙招。", "湿气重可从饮食、运动、作息多方面调理。", 892, LocalDateTime.now().minusDays(7)),
                new ArticleResponse(3L, "一碗好汤，胜过良药：汤方的智慧", "药膳食疗", "", "汤方食疗是日常滋补的重要组成部分。", "汤方讲究因时、因人、因体质搭配。", 1560, LocalDateTime.now().minusDays(10))
        );
    }

    public ArticleResponse article(Long id) {
        return articles().stream()
                .filter(article -> article.id().equals(id))
                .findFirst()
                .orElse(articles().get(0));
    }

    public List<FavoriteResponse> favorites() {
        return new ArrayList<>(favorites);
    }

    public FavoriteResponse addFavorite(FavoriteRequest request) {
        RecipeResponse recipe = recipe(request.targetId());
        FavoriteResponse favorite = new FavoriteResponse(
                favoriteIds.incrementAndGet(),
                request.targetType(),
                request.targetId(),
                recipe.name(),
                recipe.summary(),
                LocalDateTime.now()
        );
        favorites.add(favorite);
        return favorite;
    }

    public void removeFavorite(Long id) {
        favorites.removeIf(favorite -> favorite.id().equals(id));
    }

    public List<RecipeResponse> allRecipes() {
        return List.of(
                recipe(1L, "当归黄芪乌鸡汤", "medicated_food", "养生", List.of("补气养血", "健脾益气", "温补"), "以当归、黄芪与乌鸡同炖，适合气血不足、疲劳乏力人群。", "约 2.5 小时", 982, "四季"),
                recipe(2L, "桂枝汤", "soup", "安神", List.of("解表散寒", "温通经脉"), "温通经络，缓解风寒疲乏，适合头痛身酸人群。", "约 60 分钟", 765, "冬季"),
                recipe(3L, "莲子百合银耳羹", "medicated_food", "安神", List.of("养心安神", "润肺养颜"), "清润平和，适合心烦失眠、干燥上火者。", "约 45 分钟", 1120, "夏季"),
                recipe(4L, "玫瑰红枣奶茶", "tea", "养生", List.of("补气养血", "美容养颜"), "玫瑰理气，红枣补血，适合气色不佳的温润茶饮。", "约 15 分钟", 890, "四季"),
                recipe(5L, "茯苓山药排骨汤", "soup", "祛湿", List.of("健脾祛湿", "增强免疫"), "茯苓与山药配伍，健脾化湿，适合春季湿重人群。", "约 90 分钟", 820, "春季"),
                recipe(6L, "四物汤", "soup", "养生", List.of("补血养血", "调经止痛"), "当归、川芎、白芍、熟地组合，适合女性调养。", "约 70 分钟", 710, "秋季"),
                recipe(7L, "冬瓜薏米汤", "soup", "减脂", List.of("清热利湿", "轻盈体态"), "清淡不腻，适合希望减脂塑形与祛湿的人群。", "约 50 分钟", 680, "夏季"),
                recipe(8L, "陈皮普洱茶", "tea", "祛湿", List.of("理气健脾", "助消化"), "陈皮理气，普洱温润，适合饭后饮用。", "约 10 分钟", 750, "四季"),
                recipe(9L, "桂圆枸杞茶", "tea", "安神", List.of("补益心脾", "养血安神"), "桂圆与枸杞搭配，适合熬夜后温养调理。", "约 12 分钟", 930, "冬季")
        );
    }

    private RecipeResponse recipe(Long id, String name, String type, String category, List<String> tags,
                                  String summary, String cookingTime, int popularity, String season) {
        return new RecipeResponse(
                id,
                name,
                type,
                category,
                "",
                summary,
                tags,
                List.of("当归", "黄芪", "红枣", "枸杞", "生姜"),
                "食材洗净后入锅，加清水慢炖，出锅前少许盐调味。",
                String.join("、", tags),
                "适合亚健康、体虚易疲劳或日常调理人群",
                "感冒发热、孕期或特殊疾病人群请咨询专业医师",
                cookingTime,
                popularity,
                season
        );
    }
}
