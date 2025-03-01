package com.ict.vita.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Result클래스의 response 필드값 설정 클래스로
 * 컨트롤러 요청 처리를 성공했을때와 실패했을때에 반환하는 결과 객체(Result)의 response필드에 담을 데이터를 다르게 하기 위함
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
