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
	
	//임시로 저장할때 사용하는 값
	public static final String TEMPORARY = "TEMPORARY";

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
