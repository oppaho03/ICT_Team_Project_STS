package com.ict.vita.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//[AES256 암호화 테스트용]
@RestController
public class AesTestController {
	@PostMapping("/aestest")
	public ResponseEntity<?> aesTest(@RequestBody Map<String, String> map) throws Exception{
		System.out.println("AES 암호화 하기 전 평문값:"+map.get("value"));
		String encryptedValue = AES256Util.encrypt(map.get("value"));
		System.out.println("AES 암호화 후:"+encryptedValue);
		System.out.println("AES 복호화 후:"+AES256Util.decrypt(encryptedValue));
		System.out.println("====================================");
		return ResponseEntity.status(HttpStatus.OK).body(encryptedValue);
	}
}
