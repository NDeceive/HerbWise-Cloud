package com.zhishanfang.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "health_profiles")
public class HealthProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private String gender;
    private String birthDate;
    private Integer age;
    private Double height;
    private Double weight;
    @Lob
    private String constitutionTypes;
    @Lob
    private String symptoms;
    @Lob
    private String diseases;
    private String tastePreference;
    private String dietRegularity;
    @Lob
    private String allergies;
    private String sleepTime;
    private String wakeTime;
    private String waterIntake;
    @Lob
    private String goals;
    @Lob
    private String extraNote;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
