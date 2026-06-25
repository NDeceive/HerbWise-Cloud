package com.zhishanfang.backend.controller;

import com.zhishanfang.backend.dto.HealthProfileRequest;
import com.zhishanfang.backend.dto.ProfileSavedResponse;
import com.zhishanfang.backend.dto.RecommendationResponse;
import com.zhishanfang.backend.model.HealthProfile;
import com.zhishanfang.backend.service.AiService;
import com.zhishanfang.backend.service.HealthProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final HealthProfileService healthProfileService;
    private final AiService aiService;
    private final Long mockUserId;

    public AiController(
            HealthProfileService healthProfileService,
            AiService aiService,
            @Value("${app.mock-user-id:1}") Long mockUserId
    ) {
        this.healthProfileService = healthProfileService;
        this.aiService = aiService;
        this.mockUserId = mockUserId;
    }

    @PostMapping("/profile")
    public ProfileSavedResponse saveProfile(@RequestBody HealthProfileRequest request) {
        HealthProfile profile = healthProfileService.save(mockUserId, request);
        return new ProfileSavedResponse(profile.getId());
    }

    @PostMapping("/recommend")
    public RecommendationResponse recommend(@RequestBody HealthProfileRequest request) {
        HealthProfile profile = healthProfileService.save(mockUserId, request);
        return aiService.recommend(mockUserId, profile);
    }

    @GetMapping("/result/{id}")
    public RecommendationResponse result(@PathVariable Long id) {
        return aiService.findById(id);
    }
}
