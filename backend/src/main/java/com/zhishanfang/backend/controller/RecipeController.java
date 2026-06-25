package com.zhishanfang.backend.controller;

import com.zhishanfang.backend.dto.RecipeResponse;
import com.zhishanfang.backend.service.RecipeService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeResponse> list(@RequestParam(required = false) String type,
                                     @RequestParam(required = false) String category) {
        return recipeService.list(type, category);
    }

    @GetMapping("/{id}")
    public RecipeResponse detail(@PathVariable Long id) {
        return recipeService.detail(id);
    }
}
