package com.ict.vita.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * 컨트롤러가 반환하는 값들을 묶어서 보내주는 클래스
 * @param <T> 컨트롤러가 반환하는 DTO 객체
 */
public class Result<T> {
	private int success;
	private T data;
}
