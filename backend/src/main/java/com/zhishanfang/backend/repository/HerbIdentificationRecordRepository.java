package com.zhishanfang.backend.repository;

import com.zhishanfang.backend.model.HerbIdentificationRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HerbIdentificationRecordRepository extends JpaRepository<HerbIdentificationRecord, Long> {
    List<HerbIdentificationRecord> findTop10ByOrderByCreatedAtDesc();
}
