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
@Table(name = "herb_identification_records")
public class HerbIdentificationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String imageName;
    private String herbName;
    private String confidenceLevel;
    @Lob
    private String appearanceFeatures;
    @Lob
    private String effects;
    @Lob
    private String suitablePeople;
    @Lob
    private String warnings;
    @Lob
    private String recommendedRecipes;
    @Lob
    private String modelRawResult;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
