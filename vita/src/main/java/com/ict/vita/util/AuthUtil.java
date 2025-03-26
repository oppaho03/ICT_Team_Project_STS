package com.ict.vita.util;

import java.util.Random;

//[인증코드 생성]
public class AuthUtil {
	
	/**[6자리 이메일 인증코드 생성 메서드]
	 */
	public static String generateEmailAuthCode() {
		Random random = new Random();
		StringBuilder code = new StringBuilder();
		
		for(int i=0;i<6;i++) {
			code.append(random.nextInt(10)); // 0~9 임의의 값 추가
		}
		
		System.out.println("generateEmailAuthCode(6자리):"+code);
		
		return code.toString();

	}
	
	/**
	 * [자리수를 인자로 받아 이메일 인증코드 생성하는 메서드]
	 * @param length 자리수
	 * @return
	 */
	public static String generateEmailAuthCode(int length) {
		Random random = new Random();
		StringBuilder code = new StringBuilder();
		
		if(length > 0) {
			for(int i=0;i<length;i++) {
				code.append(random.nextInt(10)); // 0~9 임의의 값 추가
			}
			System.out.println(String.format("generateEmailAuthCode(자릿수:%d자리):%s", length,code.toString()));
		}
		
		return code.toString();
	}

}
