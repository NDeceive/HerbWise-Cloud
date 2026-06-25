package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.HealthProfileRequest;
import com.zhishanfang.backend.model.HealthProfile;
import com.zhishanfang.backend.repository.HealthProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class HealthProfileService {
    private final HealthProfileRepository healthProfileRepository;

    public HealthProfileService(HealthProfileRepository healthProfileRepository) {
        this.healthProfileRepository = healthProfileRepository;
    }

    public HealthProfile save(Long userId, HealthProfileRequest request) {
        HealthProfile profile = new HealthProfile();
        profile.setUserId(userId);
        profile.setName(request.name());
        profile.setGender(request.gender());
        profile.setBirthDate(request.birthDate());
        profile.setAge(request.age());
        profile.setHeight(request.height());
        profile.setWeight(request.weight());
        profile.setConstitutionTypes(TextList.join(request.constitutionTypes()));
        profile.setSymptoms(TextList.join(request.symptoms()));
        profile.setDiseases(TextList.join(request.diseases()));
        profile.setTastePreference(request.tastePreference());
        profile.setDietRegularity(request.dietRegularity());
        profile.setAllergies(TextList.join(request.allergies()));
        profile.setSleepTime(request.sleepTime());
        profile.setWakeTime(request.wakeTime());
        profile.setWaterIntake(request.waterIntake());
        profile.setGoals(TextList.join(request.goals()));
        profile.setExtraNote(request.extraNote());
        return healthProfileRepository.save(profile);
    }
}
