package com.github.kmu_wink.common.api;

import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T content;

    public static <T> ApiResponse<T> ok(T content) {

        return new ApiResponse<>(true, null, content);
    }

    public static ApiResponse<Map<String, String>> error(String message) {

        return new ApiResponse<>(false, message, null);
    }
}
