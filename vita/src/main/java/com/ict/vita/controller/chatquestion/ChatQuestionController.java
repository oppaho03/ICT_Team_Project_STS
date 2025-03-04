package com.ict.vita.controller.chatquestion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatanswer.ChatAnswerService;
import com.ict.vita.service.chatquestion.ChatQuestionService;
import com.ict.vita.service.chatquestion.ChatQuestionWithSessionRequestDto;
import com.ict.vita.service.chatquestion.ChatQuestionWithSessionResponseDto;
import com.ict.vita.service.chatsession.ChatSessionDto;
import com.ict.vita.service.chatsession.ChatSessionService;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatQuestionController {
	//서비스 주입
	private final ChatQuestionService chatQuestionService;
	private final ChatSessionService chatSessionService;
	private final ChatAnswerService chatAnswerService;
	private final MemberService memberService;
	
	/**
	 * [질문과 세션 생성]
	 * @param qwsDto 질문과 세션에 대한 정보를 담고있는 DTO 객체
	 * @param token 로그인한 회원의 토큰값
	 * @return ResponseEntity
	 */
	@PostMapping("/chatquestions")
	public ResponseEntity<?> createQuestionWithSession(@RequestBody ChatQuestionWithSessionRequestDto qwsDto,@RequestHeader(name = "Authorization") String token){
		//회원의 토큰값 조회
		
		MemberDto findedMember = memberService.findMemberByToken(token);
		//<회원 토큰값이 존재하지 않는 경우>
		
	
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("유효하지 않은 토큰입니다"));
		}
		
		
		
		//<회원 토큰값이 존재하는 경우>
		Long sid;
		//세션의 존재여부 확인
		//세션id가 안 넘어온 경우
		if(qwsDto.getSid() == null) { 
			//세션 생성
			ChatSessionDto sessionDto = ChatSessionDto.builder()
													.memberDto(findedMember)
													.created_at(LocalDateTime.now())
													.updated_at(LocalDateTime.now())
													.status(1)
													.count(0)
													.build();
			ChatSessionDto createdSession = chatSessionService.createSession(sessionDto);
			sid = createdSession.getId();
		}
		//세션id가 넘어온 경우
		else {
			sid = qwsDto.getSid();
		}	
		
		//질문의 키워드로 답변 검색
		List<ChatAnswerDto> answers = chatAnswerService.findAnswerByKeywords(qwsDto.getKeywords().stream().collect(Collectors.joining(" OR ")));
		
		//<답변 검색 결과가 없는 경우>
		if(answers.size() == 0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResultUtil.fail("질문 키워드로 검색한 답변이 없습니다"));
		}
		
		System.out.println(answers.size());
		
		//<답변 검색 결과가 있는 경우>
		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(ChatQuestionWithSessionResponseDto.builder().sid(sid).answers(answers).build()));

	}
	
}
