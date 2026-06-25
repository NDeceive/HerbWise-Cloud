package com.zhishanfang.backend.controller;

import com.zhishanfang.backend.dto.FavoriteRequest;
import com.zhishanfang.backend.dto.FavoriteResponse;
import com.zhishanfang.backend.service.FavoriteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final Long mockUserId;

    public FavoriteController(FavoriteService favoriteService, @Value("${app.mock-user-id:1}") Long mockUserId) {
        this.favoriteService = favoriteService;
        this.mockUserId = mockUserId;
    }

    @GetMapping
    public List<FavoriteResponse> list() {
        return favoriteService.list(mockUserId);
    }

    @PostMapping
    public FavoriteResponse add(@RequestBody FavoriteRequest request) {
        return favoriteService.add(mockUserId, request);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        favoriteService.remove(id);
    }
}
