package com.zhishanfang.backend.service;

import java.util.Arrays;
import java.util.List;

final class TextList {
    private TextList() {
    }

    static String join(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        return String.join("、", values.stream().filter(value -> value != null && !value.isBlank()).toList());
    }

    static List<String> split(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("[、,，\\n]+"))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }
}
