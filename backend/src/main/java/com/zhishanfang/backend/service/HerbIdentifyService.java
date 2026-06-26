package com.zhishanfang.backend.service;

import com.zhishanfang.backend.dto.HerbIdentifyDetailResponse;
import com.zhishanfang.backend.dto.HerbIdentifyRecordResponse;
import com.zhishanfang.backend.dto.HerbIdentifyResponse;
import com.zhishanfang.backend.model.HerbIdentificationRecord;
import com.zhishanfang.backend.repository.HerbIdentificationRecordRepository;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HerbIdentifyService {
    private final HerbIdentificationRecordRepository recordRepository;

    public HerbIdentifyService(ObjectProvider<HerbIdentificationRecordRepository> recordRepository) {
        this.recordRepository = recordRepository.getIfAvailable();
    }

    public HerbIdentifyResponse identifyHerb(MultipartFile file) {
        HerbIdentifyResponse mockResult = callMultimodalModelForHerbRecognition(file);
        if (recordRepository == null) {
            return mockResult;
        }

        HerbIdentificationRecord record = new HerbIdentificationRecord();
        record.setUserId(null);
        record.setImageName(imageNameOf(file));
        record.setHerbName(mockResult.herbName());
        record.setConfidenceLevel(mockResult.confidenceLevel());
        record.setAppearanceFeatures(TextList.join(mockResult.appearanceFeatures()));
        record.setEffects(TextList.join(mockResult.effects()));
        record.setSuitablePeople(TextList.join(mockResult.suitablePeople()));
        record.setWarnings(TextList.join(mockResult.warnings()));
        record.setRecommendedRecipes(TextList.join(mockResult.recommendedRecipes()));
        record.setModelRawResult(modelRawResultFor(mockResult));
        return toIdentifyResponse(recordRepository.save(record));
    }

    public List<HerbIdentifyRecordResponse> getRecentRecords() {
        if (recordRepository == null) {
            return List.of(
                    mockRecord("黄芪", "补气固表", "较高"),
                    mockRecord("陈皮", "理气健脾", "中等"),
                    mockRecord("枸杞", "滋补肝肾", "较高")
            );
        }

        return recordRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(this::toRecordResponse)
                .toList();
    }

    public HerbIdentifyDetailResponse getRecordDetail(Long id) {
        if (recordRepository == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Herb identification record not found");
        }

        HerbIdentificationRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Herb identification record not found"));
        return toDetailResponse(record);
    }

    private HerbIdentifyResponse callMultimodalModelForHerbRecognition(MultipartFile file) {
        // This method will later call a multimodal large model API with the uploaded image and request structured JSON output.
        return new HerbIdentifyResponse(
                null,
                imageNameOf(file),
                "黄芪",
                "较高",
                List.of(
                        "切片呈淡黄色至黄白色",
                        "表面可见细密放射状纹理",
                        "质地较轻，边缘略不规则"
                ),
                List.of("补气固表", "健脾益气", "利水消肿"),
                List.of("气虚乏力人群", "易疲劳人群", "脾胃虚弱人群"),
                List.of(
                        "实热体质慎用",
                        "感冒发热期间不建议自行食用",
                        "慢性病或正在服药人群请咨询专业医师"
                ),
                List.of("当归黄芪乌鸡汤", "黄芪党参乌鸡汤"),
                null
        );
    }

    private HerbIdentifyResponse toIdentifyResponse(HerbIdentificationRecord record) {
        return new HerbIdentifyResponse(
                record.getId(),
                record.getImageName(),
                record.getHerbName(),
                record.getConfidenceLevel(),
                TextList.split(record.getAppearanceFeatures()),
                TextList.split(record.getEffects()),
                TextList.split(record.getSuitablePeople()),
                TextList.split(record.getWarnings()),
                TextList.split(record.getRecommendedRecipes()),
                record.getCreatedAt()
        );
    }

    private HerbIdentifyRecordResponse toRecordResponse(HerbIdentificationRecord record) {
        return new HerbIdentifyRecordResponse(
                record.getId(),
                record.getImageName(),
                record.getHerbName(),
                record.getConfidenceLevel(),
                TextList.split(record.getAppearanceFeatures()),
                TextList.split(record.getEffects()),
                TextList.split(record.getSuitablePeople()),
                TextList.split(record.getWarnings()),
                TextList.split(record.getRecommendedRecipes()),
                record.getCreatedAt()
        );
    }

    private HerbIdentifyDetailResponse toDetailResponse(HerbIdentificationRecord record) {
        return new HerbIdentifyDetailResponse(
                record.getId(),
                record.getImageName(),
                record.getHerbName(),
                record.getConfidenceLevel(),
                TextList.split(record.getAppearanceFeatures()),
                TextList.split(record.getEffects()),
                TextList.split(record.getSuitablePeople()),
                TextList.split(record.getWarnings()),
                TextList.split(record.getRecommendedRecipes()),
                record.getCreatedAt()
        );
    }

    private String imageNameOf(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            return "uploaded-image";
        }
        return file.getOriginalFilename();
    }

    private String modelRawResultFor(HerbIdentifyResponse response) {
        return "mock herb recognition result: " + response.herbName() + " / " + response.confidenceLevel();
    }

    private HerbIdentifyRecordResponse mockRecord(String herbName, String effect, String confidenceLevel) {
        return new HerbIdentifyRecordResponse(
                null,
                null,
                herbName,
                confidenceLevel,
                List.of(),
                List.of(effect),
                List.of(),
                List.of(),
                List.of(),
                null
        );
    }
}
