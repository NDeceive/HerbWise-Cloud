package com.zhishanfang.backend.dto;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String title,
        String category,
        String cover,
        String summary,
        String content,
        int views,
        LocalDateTime createdAt
) {
}
