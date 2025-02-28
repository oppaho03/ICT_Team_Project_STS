package com.ict.vita.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Result클래스의 response 필드값 설정 클래스
 */
public class ResultUtil {

	//성공 응답 (data 포함)
	// T는 DTO객체
    public static <T> Result<Map<String, T>> success(T data) {
        Map<String, T> responseData = new HashMap<>();
        responseData.put("data", data);
        return new Result<>(1, responseData);
    }

    //실패 응답 (message 포함)
    public static Result<Map<String, String>> fail(String message) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", message);
        return new Result<>(0, errorMessage);
    }
}
