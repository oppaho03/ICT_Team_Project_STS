package com.ict.vita.controller.terms;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberJoinDto;
import com.ict.vita.service.terms.TermAddDto;
import com.ict.vita.service.terms.TermsDto;
import com.ict.vita.service.terms.TermsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.JwtUtil;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
//[용어 Controller]
public class TermsController {
	//서비스 주입
	private final TermsService termsService;
	private final JwtUtil jwtutil; // Constructor Injection, JwtUtil
	
	/**
	 * 용어(APP_TERMS) 전체 리스트 검색
	 * @return null 또는 배열 반환
	 */
	@Operation( summary = "용어(APP_TERMS) 전체 리스트 검색", description = "용어(APP_TERMS) 전체 리스트 검색" )
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = TermsDto.class),
				examples = @ExampleObject(
						value = "{ \"success\": 1, \"response\": { \"data\": [{\"id\": 1, \"name\": \"감염성질환\", \"slug\": \"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\", \"group_number\": 0 }] } }" 
				)
			)
		),
		@ApiResponse(
			responseCode = "200",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = TermsDto.class),
				examples = @ExampleObject(
						value = "{ \"success\": 1, \"response\": { \"data\": null } }" 
				)
			)
		) 
	})
	@GetMapping("/")
//	public ResponseEntity<?> getAllList(@RequestHeader(value = "Authorization", required = true) String authHeader){
	public ResponseEntity<?> getAllList(){
		List<TermsDto> temrs = termsService.findAll();
		
		if ( temrs == null || temrs.isEmpty() ) temrs = null;
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( temrs ));
		
		// Authorization: Bearer __토큰값__]
//		try {
//			/*
//			 * JWT 토큰 유효성 검사 및 파싱
//			 */			
//			String token = authHeader.replace("Bearer ", ""); // Bearer 삭제
//			System.out.println(token);
//			
////			if ( ! jwtutil.IsValidToken(token) ) throw new Exception();
//			
//			
//			List<TermsDto> temrs = termsService.findAll();
//			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(temrs));
//		}
//		catch( Exception e ) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("유효한 토큰이 아닙니다."));
//		}
	}
	
	/**
	 * 용어(APP_TERMS) 이름(name)으로 검색
	 * @param name - 용어(APP_TERMS) 이름
	 * @return null 또는 배열 반환
	 */
	@Operation( summary = "용어(APP_TERMS) 이름(name)으로 검색", description = "용어(APP_TERMS) 이름(name)으로 검색" )
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = TermsDto.class),
				examples = @ExampleObject(
						value = "{ \"success\": 1, \"response\": { \"data\": [{\"id\": 1, \"name\": \"감염성질환\", \"slug\": \"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\", \"group_number\": 0 }] } }" 
				)
			)
		),
		@ApiResponse(
			responseCode = "200",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = TermsDto.class),
				examples = @ExampleObject(
						value = "{ \"success\": 1, \"response\": { \"data\": null } }" 
				)
			)
		) 
	})
	@GetMapping("/name/{name}")
	public ResponseEntity<?> getTermsByName(@PathVariable String name ) {
		List<TermsDto> temrs = termsService.findAllByName( name );
		if ( temrs == null || temrs.isEmpty() ) temrs = null;
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( temrs ));
	}
	
	/**
	 * 	
	 */
	@PostMapping("/add")
	public ResponseEntity<?> addTerm( @RequestBody @Valid TermAddDto data, BindingResult bindingResult ) {
		
		if(bindingResult.hasErrors()) {
		
		}
		
		if ( Commons.isNull(data.getSlug()) ) 
			data.setSlug( data.getName() ); // slug 값 입력되지 않은 경우, 이름을 URLEncode 하여 사용
		
		String slug = data.getSlug();
		try {
			slug = URLDecoder.decode(slug, "UTF-8"); // 기본 (original)
			data.setSlug( URLEncoder.encode(slug, "UTF-8") ); // URLEncode 
			
			// "slug" 중복 검사
			String tax = Commons.isNull(data.getCategory()) ? "category" : data.getCategory();
						
			termsService.findAllBySlug( data.getSlug(), tax ); // 'slug' -> URLEncode
			
			
		}
		catch (Exception ex) {
			
		}
		
		
		
//		String encoded = URLEncoder.encode(input, "UTF-8");
		
		
		return null;
	}
}
