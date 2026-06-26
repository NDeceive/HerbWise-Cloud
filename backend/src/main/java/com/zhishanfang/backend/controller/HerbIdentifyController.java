package com.zhishanfang.backend.controller;

import com.zhishanfang.backend.dto.HerbIdentifyDetailResponse;
import com.zhishanfang.backend.dto.HerbIdentifyRecordResponse;
import com.zhishanfang.backend.dto.HerbIdentifyResponse;
import com.zhishanfang.backend.service.HerbIdentifyService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/herb")
public class HerbIdentifyController {
    private final HerbIdentifyService herbIdentifyService;

    public HerbIdentifyController(HerbIdentifyService herbIdentifyService) {
        this.herbIdentifyService = herbIdentifyService;
    }

    @PostMapping("/identify")
    public HerbIdentifyResponse identify(@RequestParam("file") MultipartFile file) {
        return herbIdentifyService.identifyHerb(file);
    }

    @GetMapping("/records")
    public List<HerbIdentifyRecordResponse> records() {
        return herbIdentifyService.getRecentRecords();
    }

    @GetMapping("/records/{id}")
    public HerbIdentifyDetailResponse recordDetail(@PathVariable Long id) {
        return herbIdentifyService.getRecordDetail(id);
    }
}
