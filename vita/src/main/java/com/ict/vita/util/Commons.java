package com.ict.vita.util;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public class Commons {
	/**
	 * 문자열이 null 또는 빈문자열인지 판단하는 함수
	 * @param string 받은 문자열
	 * @return 판단여부
	 */
	public static boolean isNull(String string) {
		return ( string == null || string.isEmpty() || string.trim().length() == 0 ) ? true : false;
	}

	/**
	 * BindingResult 결과 포맷 
	 * @param bindingResult BindingResult
	 * @param message String
	 * @return 
	 */
	public static String formatBindingResultHasError( BindingResult bindResult ) {
		return bindResult.hasErrors() ? String.format( 
			"Invalid Error : "
			+ bindResult.getAllErrors().stream().map( e -> e.getDefaultMessage() ).collect( Collectors.joining(",") )
		) : null;
	}

	/**
	 * BindingResult 결과 포맷 
	 * @param bindingResult BindingResult
	 * @param message String
	 * @return 
	 */
	public static String formatBindingResultHasError( BindingResult bindResult, String message) {
		if ( Commons.isNull(message) ) message = "Invalid Error";
		return bindResult.hasErrors() ? String.format( 
			"%s : "
			+ bindResult.getAllErrors().stream().map( e -> e.getDefaultMessage() ).collect( Collectors.joining(",") )
			, message
		) : null;
	}
	
}
