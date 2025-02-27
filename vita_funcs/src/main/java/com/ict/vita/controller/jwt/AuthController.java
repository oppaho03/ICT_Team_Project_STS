package com.ict.vita.controller.jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.util.jwt.JwtUtil;


/**
 * 로그인 요청을 처리하는 컨트롤러
 * `/auth/login` 엔드포인트에서 JWT를 생성하여 반환
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private JwtUtil jwtutil;
	
	/**
	 *테스트용 사용자 데이터
	 */
	
	private static final Map<String, String> users = new HashMap<>();
	static {
		users.put("testuser", "password123");
		users.put("admin", "admin123");
	}
	  /**
     * 로그인 API (`/auth/login`)
     * @param username 사용자 ID
     * @param password 사용자 비밀번호
     * @return JWT 토큰
     */	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String username,@RequestParam String passowrd){
		
		if(!users.containsKey(username) || !users.get(username).equals(passowrd)) {
			return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다");
		
		}
		Map<String, Object> claims = new HashMap<>();
		claims.put("role",username.equalsIgnoreCase("admin") ? "ADMIN":"USER");
		
		
		String token = jwtutil.CreateToken(username,claims );
		return ResponseEntity.ok().body("Bearer"+token);
		
	}

	

}
