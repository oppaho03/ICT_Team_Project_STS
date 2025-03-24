package com.ict.vita.util;

import java.util.Random;

//[인증코드 생성]
public class AuthCode {
	
	//6자리 인증코드 생성 메서드
	public static String generateAuthCode() {
		Random random = new Random();
		String code = String.format("%06d", random.nextInt(1000000));
		System.out.println("6자리 인증코드:"+code);
		
		return code;
	}
	
	//자리수를 인자로 받아 인증코드 생성하는 메서드
	public static String generateAuthCode(int length) {
		Random random = new Random();
		
		if(length > 0) {
			int num = 1;
			
			for(int i=0;i<length;i++) {
				num *= 10;
			}
			
			String code = String.format("%0"+length+"d", random.nextInt(num));
			System.out.println("자리수 인자로 받아 인증코드 생성:"+code);
			
			return code;
		}
		
		return null;
	}

}
