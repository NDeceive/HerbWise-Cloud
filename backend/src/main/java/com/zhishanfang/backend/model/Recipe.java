package com.zhishanfang.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String category;
    private String imageUrl;
    @Lob
    private String summary;
    @Lob
    private String tags;
    @Lob
    private String ingredients;
    @Lob
    private String steps;
    @Lob
    private String effects;
    @Lob
    private String suitablePeople;
    @Lob
    private String contraindications;
    private String cookingTime;
    private Integer popularity;
    private String season;
}
