package com.ict.vita.util;

import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;

public class Commons {
	
	//[임시로 저장할때 사용하는 값들]
	//<회원가입 관련>
	public static final String AUTHORIZATION = "Authorization"; //헤더에서 토큰값 네임
	public static final String TEMPORARY = "TEMPORARY"; //임시 회원가입시 사용
	public static final String ROLE_USER = "USER"; //회원가입 역할 - 일반 회원
	public static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR"; //회원가입 역할 - 관리자
	//<글(포스트) 관련>
	public static final String POST_STATUS_PUBLISH = "PUBLISH"; //글 공개
	public static final String POST_STATUS_PRIVATE = "PRIVATE"; //글 비공개
	public static final String POST_STATUS_DELETE = "DELETE"; //글 삭제
	//<댓글 관련> - 사용할지 말지 결정 안됨
	public static final String COMMENT_STATUS_OPEN = "OPEN"; //댓글 허용
	

	/**
	 * 문자열이 null 또는 빈문자열인지 판단하는 함수
	 * @param string 받은 문자열
	 * @return 판단여부
	 */
	public static boolean isNull(String string) {
		return ( string == null || string.isEmpty() || string.trim().length() == 0 ) ? true : false;
	}


	/**
	 * i18n 을 사용한 다국어 (언어 키만 사용)
	 * @param messageSource
	 * @param key
	 */
	public static String i18nMessages(MessageSource messageSource, String key) { return messageSource.getMessage(key, null, new Locale("ko")); }

	/**
	 * i18n 을 사용한 다국어 
	 * @param messageSource
	 * @param key
	 * @param args 
	 * @param locale
	 */
	public static String i18nMessages(MessageSource messageSource, String key, @Nullable Object[] args, @Nullable Locale locale ) {	return messageSource.getMessage(key, args, locale == null ? new Locale("ko") : locale ); }


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
		if ( isNull(message) ) message = "Invalid Error";
		return bindResult.hasErrors() ? String.format( 
			"%s : "
			+ bindResult.getAllErrors().stream().map( e -> e.getDefaultMessage() ).collect( Collectors.joining(",") )
			, message
		) : null;
	}


	/**
	 * HTTP Header 파서(Parser)
	 * @param toekn String 
	 * @return 
	 */
	public static String parseHTTPHeaderToken( String token ) { return isNull(token) ? null : token.replace("Bearer ", ""); }
	
	/**
	 * [토큰값으로 회원 조회]
	 * @param token 서버로 넘어온 토큰값
	 * @param memberService 토큰값으로 회원을 찾기 위한 회원 서비스
	 * @return MemberDto 토큰이 유효하면 MemberDto를, 유효하지 않으면 null 반환
	 */
	public static MemberDto findMemberByToken(String token,MemberService memberService) {
		MemberDto findedMember = memberService.findMemberByToken(parseHTTPHeaderToken(token));
		//<찾은 회원이 존재하는 경우>
		if(findedMember != null)
			return findedMember;
		//<찾은 회원이 존재하지 않는 경우>
		return null;
	}
	
}
