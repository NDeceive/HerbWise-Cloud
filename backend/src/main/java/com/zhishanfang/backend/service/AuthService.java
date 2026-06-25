package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.AuthRequest;
import com.zhishanfang.backend.dto.UserResponse;
import com.zhishanfang.backend.model.User;
import com.zhishanfang.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ResponseMapper mapper;

    public AuthService(UserRepository userRepository, ResponseMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public UserResponse login(AuthRequest request) {
        String phone = normalizedPhone(request);
        User user = userRepository.findByPhone(phone).orElseGet(() -> createUser(phone, "张女士"));
        return mapper.toUser(user);
    }

    public UserResponse register(AuthRequest request) {
        String phone = normalizedPhone(request);
        User user = userRepository.findByPhone(phone).orElseGet(() -> createUser(phone, "新用户"));
        return mapper.toUser(user);
    }

    public UserResponse guest() {
        return new UserResponse(0L, "", "游客", "");
    }

    private String normalizedPhone(AuthRequest request) {
        if (request == null || request.phone() == null || request.phone().isBlank()) {
            return "13800000000";
        }
        return request.phone();
    }

    private User createUser(String phone, String nickname) {
        User user = new User();
        user.setPhone(phone);
        user.setNickname(nickname);
        user.setAvatar("");
        return userRepository.save(user);
    }
}
