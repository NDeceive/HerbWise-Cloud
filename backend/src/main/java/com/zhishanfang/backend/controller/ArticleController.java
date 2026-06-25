package com.zhishanfang.backend.controller;

import com.zhishanfang.backend.dto.ArticleResponse;
import com.zhishanfang.backend.service.ArticleService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleResponse> list() {
        return articleService.list();
    }

    @GetMapping("/{id}")
    public ArticleResponse detail(@PathVariable Long id) {
        return articleService.detail(id);
    }
}
