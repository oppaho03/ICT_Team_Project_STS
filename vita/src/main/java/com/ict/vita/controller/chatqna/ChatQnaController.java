package com.ict.vita.controller.chatqna;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatqna.ChatQnaDto;
import com.ict.vita.service.chatqna.ChatQnaService;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatQnaController {
	//서비스 주입
	private final ChatQnaService chatQnaService;
	private final MemberService memberService;
	
	/**
	 * [세션id(PK)로 QNA테이블에서 질문-답변 쌍 조회]
	 * @param token 로그인한 회원의 토큰값
	 * @param sid 세션id(PK)로 쿼리파라미터로 넘어옴
	 * @return
	 */
	@GetMapping("/sessions")
	public ResponseEntity<?> getQna(@RequestHeader(name = "Authorization") String token, @RequestParam Long sid) {
		//회원의 토큰값 조회
		MemberDto findedMember = memberService.findMemberByToken(token);
		//<회원 토큰값이 존재하지 않는 경우>
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("유효하지 않은 토큰입니다"));
		}
		
		//<회원 토큰값이 존재하는 경우>
		//세션id로 qna 조회
		List<ChatQnaDto> findedQna = chatQnaService.findAllBySession(sid);
		
		for(ChatQnaDto dto: findedQna) {
			System.out.println("++++ dto:"+dto.getId());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(findedQna));
	}
	
}
