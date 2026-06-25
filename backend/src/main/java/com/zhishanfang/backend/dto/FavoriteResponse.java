package com.zhishanfang.backend.dto;

import java.time.LocalDateTime;

public record FavoriteResponse(
        Long id,
        String targetType,
        Long targetId,
        String title,
        String summary,
        LocalDateTime createdAt
) {
}
