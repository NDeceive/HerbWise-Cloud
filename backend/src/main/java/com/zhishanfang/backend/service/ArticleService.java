package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.ArticleResponse;
import com.zhishanfang.backend.repository.ArticleRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ResponseMapper mapper;

    public ArticleService(ArticleRepository articleRepository, ResponseMapper mapper) {
        this.articleRepository = articleRepository;
        this.mapper = mapper;
    }

    public List<ArticleResponse> list() {
        return articleRepository.findAllByOrderByCreatedAtDesc().stream().map(mapper::toArticle).toList();
    }

    public ArticleResponse detail(Long id) {
        return articleRepository.findById(id)
                .map(mapper::toArticle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
    }
}
