package com.ict.vita.util.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtUtil {

	private static final String SECRET_KEY = "this-is-a-very-secure-and-long-secret-key-used-for-jwt-authentication";
	
	private static final long EXPIRATION_TIME = 900000;
	
	private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	
    /**
     * JWT 토큰을 생성하는 메서드
     * @param username 사용자 ID
     * @param claims 추가적인 사용자 정보 (예: 역할)
     * @return "Bearer " + JWT 토큰 (인증 헤더에서 사용 가능하도록)
     */
	
	public String CreateToken(String username, Map<String, Object> claims) {
		
		return  Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
		
	}
    /**
     * JWT 검증 및 페이로드 반환 메서드
     * @param token 클라이언트에서 받은 JWT
     * @return Claims (페이로드) 또는 null (유효하지 않은 경우)
     */
	
	public Claims ParseToken(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key).build()
					.parseClaimsJws(token)
					.getBody();
		} catch(JwtException e) {
			System.out.println("JWT 검증 실패"+e.getMessage());
			return null;
		}
	}
    /**
     * ✅ JWT 토큰이 유효한지 검증하는 메서드
     * @param token 클라이언트에서 받은 JWT
     * @return 유효한 토큰이면 true, 그렇지 않으면 false
     */
	public boolean IsValidToken(String token) {
		return ParseToken(token) != null;
	}
	
	
}
	

	

