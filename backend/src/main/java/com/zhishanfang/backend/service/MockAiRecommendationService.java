package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.HealthProfileRequest;
import com.zhishanfang.backend.dto.RecipeResponse;
import com.zhishanfang.backend.dto.RecommendationResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class MockAiRecommendationService implements AiRecommendationService {
    private final AtomicLong ids = new AtomicLong(1000);
    private final Map<Long, RecommendationResponse> results = new ConcurrentHashMap<>();
    private final MockDataService mockDataService;

    public MockAiRecommendationService(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    @Override
    public RecommendationResponse recommend(HealthProfileRequest request) {
        RecipeResponse recipe = selectRecipe(request);
        Long id = ids.incrementAndGet();
        RecommendationResponse response = new RecommendationResponse(
                id,
                recipe.name(),
                "根据您填写的体质、症状与调理目标，本方案以" + recipe.name() + "作为主推荐，兼顾安全性与日常可执行性。",
                recipe.tags(),
                recipe.ingredients(),
                List.of(
                        "乌鸡或主食材清洗后焯水去腥。",
                        "加入当归、黄芪、红枣、枸杞等食材，加清水约 1500ml。",
                        "大火煮沸后转小火慢炖，出锅前少许盐调味。"
                ),
                List.of(
                        "本平台建议仅供参考，不能替代专业医疗诊断。",
                        "感冒发热、孕期、慢性病急性发作期请先咨询专业医师。",
                        "若对相关食材过敏，请避免食用。"
                ),
                "当前健康画像偏向" + String.join("、", safeList(request.constitutionTypes())) + "，主要诉求为" + String.join("、", safeList(request.goals())) + "，推荐以温和调理为主。",
                mockDataService.recipes(null, null).stream().filter(item -> !item.id().equals(recipe.id())).limit(3).toList(),
                mockDataService.recipes("tea", null).stream().limit(3).toList(),
                LocalDateTime.now()
        );
        results.put(id, response);
        return response;
    }

    @Override
    public RecommendationResponse findById(Long id) {
        return results.computeIfAbsent(id, ignored -> recommend(defaultRequest()));
    }

    private RecipeResponse selectRecipe(HealthProfileRequest request) {
        List<String> markers = safeList(request.constitutionTypes());
        markers.addAll(safeList(request.symptoms()));
        markers.addAll(safeList(request.goals()));
        String text = String.join(",", markers);
        if (text.contains("湿热") || text.contains("容易上火") || text.contains("祛湿")) {
            return mockDataService.recipe(5L);
        }
        if (text.contains("失眠") || text.contains("安神") || text.contains("改善睡眠")) {
            return mockDataService.recipe(3L);
        }
        if (text.contains("减脂塑形")) {
            return mockDataService.recipe(7L);
        }
        return mockDataService.recipe(1L);
    }

    private List<String> safeList(List<String> value) {
        return value == null ? new java.util.ArrayList<>() : new java.util.ArrayList<>(value);
    }

    private HealthProfileRequest defaultRequest() {
        return new HealthProfileRequest(
                "张女士",
                "女",
                "1998-01-01",
                28,
                165.0,
                55.0,
                List.of("气虚质"),
                List.of("疲劳乏力"),
                List.of("无慢性病"),
                "清淡",
                "规律",
                List.of("无"),
                "23:00",
                "07:00",
                "1500ml",
                List.of("补气养血"),
                ""
        );
    }
}
