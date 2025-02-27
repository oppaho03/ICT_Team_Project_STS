package com.ict.vita.jwt.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ict.vita.jwt.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *  JWT 인증 필터 (Spring Security 필터 체인에서 실행)
 * 요청의 Authorization 헤더에서 JWT를 추출하여 검증 후, 인증 정보를 SecurityContextHolder에 저장
 * Spring Security의 `OncePerRequestFilter`를 상속하여 **한 요청당 한 번만 실행되도록 보장**
 */

public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtutil;
	
	
	public JwtFilter(JwtUtil jwtutil) {
		this.jwtutil = jwtutil;
	}

    /**	
     *  HTTP 요청을 가로채서 JWT 검증을 수행하는 메서드
     *  `HttpServletRequest`에서 Authorization 헤더를 읽어 JWT 검증 진행
     *  JWT가 유효하지 않으면 `401 Unauthorized` 응답을 반환하여 요청 차단
     *  JWT가 유효하면 Security Context에 사용자 정보를 저장하여 인증된 요청으로 처리
     *
     * @param request  클라이언트의 HTTP 요청 객체
     * @param response 서버의 HTTP 응답 객체
     * @param filterChain 필터 체인 (다음 필터로 요청을 넘기기 위해 필요)
     * @throws ServletException 필터 처리 중 발생하는 예외
     * @throws IOException      입출력 예외 처리
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			String token = request.getHeader("Authorization");
		
			if(token != null && token.startsWith("Bearer ")) {
				token= token.substring(7);
			
				Claims claims = jwtutil.ParseToken(token);
				if(claims == null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"유효하지 않은 토큰입니다");
					return;
				}
			
				String username = claims.getSubject();
				String role = claims.get("role",String.class);
			
				request.setAttribute(username, "username");
				request.setAttribute(role, "role");
		
			}
			filterChain.doFilter(request, response);
		
	}

}
