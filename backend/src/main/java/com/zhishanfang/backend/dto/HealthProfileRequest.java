package com.zhishanfang.backend.dto;

import java.util.List;

public record HealthProfileRequest(
        String name,
        String gender,
        String birthDate,
        Integer age,
        Double height,
        Double weight,
        List<String> constitutionTypes,
        List<String> symptoms,
        List<String> diseases,
        String tastePreference,
        String dietRegularity,
        List<String> allergies,
        String sleepTime,
        String wakeTime,
        String waterIntake,
        List<String> goals,
        String extraNote
) {
}
