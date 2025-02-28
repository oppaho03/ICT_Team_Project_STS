package com.ict.vita.util;

public class Commons {
	/**
	 * 문자열이 null 또는 빈문자열인지 판단하는 함수
	 * @param string 받은 문자열
	 * @return 판단여부
	 */
	public static boolean isNull(String string) {
		return ( string == null || string.isEmpty() || string.trim().length() == 0 ) ? true : false;
	}
	
}
