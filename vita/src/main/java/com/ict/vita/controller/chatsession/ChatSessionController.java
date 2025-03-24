package com.ict.vita.controller.chatsession;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatsession.ChatSessionDto;
import com.ict.vita.service.chatsession.ChatSessionResponseDto;
import com.ict.vita.service.chatsession.ChatSessionService;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class ChatSessionController {
	//서비스 주입
	private final ChatSessionService chatSessionService;
	private final MemberService memberService;
	
	private final MessageSource messageSource;
	
	/**
	 * [전체 세션 조회] - 관리자만 전체 세션 조회 가능 & 페이징 적용
	 * @param token 로그인한 회원 토큰
	 * @return ResponseEntity
	 */
	@GetMapping("/sessions")
	public ResponseEntity<?> getAllSessions(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol){
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		//<관리자가 아닌 경우>
		if( !Commons.ROLE_ADMINISTRATOR.equals(loginMember.getRole()) ) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( messageSource.getMessage("user.invalid_role", null, new Locale("ko")) ));
		}
		
		//<관리자인 경우>	
		List<ChatSessionDto> sessions;
		//전체 세션 조회
		if(p > 0)
			sessions = chatSessionService.findAll(p,ol);
		else 
			sessions = chatSessionService.findAll();
			
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(sessions.stream().map(dto -> ChatSessionResponseDto.toDto(dto)).toList() ));
		
	}
	
	/**
	 * [세션id로 세션 조회] - 본인 세션은 다 조회,남의 세션은 공개만 조회,관리자는 다 조회
	 * @param token 로그인한 회원 토큰
	 * @param id 세션id
	 * @return ResponseEntity
	 */
	@GetMapping("/sessions/{id}")
	public ResponseEntity<?> getSessionById(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "세션 id") @PathVariable("id") Long sid){
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		//세션 조회
		ChatSessionDto sessionDto = chatSessionService.findById(sid);
		//세션 미존재시
		if(sessionDto == null) return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
		//<관리자거나 자신의 세션인 경우> - 공개/비공개 세션 조회 가능
		if(loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) || loginMember.getId() == sessionDto.getMemberDto().getId() ) {
			//세션 조회(공개/비공개)
			ChatSessionDto session = chatSessionService.findById(sid);
			
			return session != null ? ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(ChatSessionResponseDto.toDto(session))) :
				ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
		
		//<자신의 세션이 아닌 경우> - 공개된 세션인 경우만 조회 가능
		ChatSessionDto findedSession = chatSessionService.getPublicById(sid);
		//공개 세션이 아닌 경우
		if(findedSession == null)
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		
		//공개 세션인 경우
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(ChatSessionResponseDto.toDto(findedSession)));

	}
	
	/**
	 * [회원id로 세션 조회] - 본인 세션은 다 조회,남의 세션은 공개만 조회,관리자는 다 조회 & 페이징 적용
	 * @param token 로그인한 회원 토큰
	 * @param mid 회원id
	 * @return ResponseEntity
	 */
	@GetMapping("/sessions/member/{id}")
	public ResponseEntity<?> getSessionsByMember(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "회원 id") @PathVariable("id") Long mid,
			@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
			@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol){
		//로그인한 회원 조회
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		//<관리자거나 자신의 세션인 경우> - 공개/비공개 세션 조회 가능 & 페이징 적용
		List<ChatSessionDto> sessions = null;
		if(loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) || loginMember.getId() == mid ) {
			if(p > 0) sessions = chatSessionService.findAllByMember(mid,p,ol);
			else sessions = chatSessionService.findAllByMember(mid);
			
		}	
		else { //<자신의 세션이 아닌 경우> - 공개된 세션인 경우만 조회 가능 & 페이징 적용
			if(p > 0) sessions = chatSessionService.findPublicsByMember(mid,p,ol);
			else sessions = chatSessionService.findPublicsByMember(mid);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body( ResultUtil.success( sessions.stream().map(dto -> ChatSessionResponseDto.toDto(dto)).toList() ) );
	}
	
}
