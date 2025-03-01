package com.ict.vita.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * 컨트롤러에서 반환하는 결과를 HTTP Message body에 담을 클래스
 * @param <T> 컨트롤러가 반환하는 Map객체
 */
public class Result<T> {
	private int success; // 성공:1/실패:0
	private T response; // data 또는 message 포함 객체
}
