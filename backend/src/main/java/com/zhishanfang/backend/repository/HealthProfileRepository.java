package com.zhishanfang.backend.repository;

import com.zhishanfang.backend.model.HealthProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthProfileRepository extends JpaRepository<HealthProfile, Long> {
    Optional<HealthProfile> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
