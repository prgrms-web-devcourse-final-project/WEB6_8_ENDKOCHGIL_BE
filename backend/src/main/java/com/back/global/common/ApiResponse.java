package com.back.global.common;

import lombok.Builder;

//API 응답 표준화
@Builder
public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T content
) {
    public ApiResponse(String code, String message) {
        this(code, message, null);
    }

    public ApiResponse(String code, String message, T content) {
        this(code.startsWith("2"), code, message, content);
    }

    // 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(String code, String message) {
        return success(code, message, null);
    }

    public static <T> ApiResponse<T> success(String code, String message, T content) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .content(content)
                .build();
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> fail(String code, String message) {
        return fail(code, message, null);
    }

    public static <T> ApiResponse<T> fail(String code, String message, T content) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .content(content)
                .build();
    }
}