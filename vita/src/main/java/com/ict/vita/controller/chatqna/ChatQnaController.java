package com.ict.vita.controller.chatqna;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatqna.ChatQnaDto;
import com.ict.vita.service.chatqna.ChatQnaService;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
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
	@Operation( summary = "질문-답변 쌍 조회", description = "질문-답변 쌍 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-조회 성공",
			description = "SUCCESS", 
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = ChatQnaDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":6,\"chatSessionDto\":{\"id\":3,\"memberDto\":{\"id\":33,\"email\":\"hoho1@naver.com\",\"password\":\"pwd\",\"role\":\"USER\",\"name\":\"홍길동\",\"nickname\":\"hoho1\",\"birth\":\"2025-03-01\",\"gender\":\"F\",\"contact\":\"01015635422\",\"address\":null,\"token\":\"testtoken\",\"created_at\":\"2025-03-01T11:36:15.801352\",\"updated_at\":\"2025-03-01T11:36:15.755014\",\"status\":9},\"created_at\":\"2025-03-04T18:56:48.993578\",\"updated_at\":\"2025-03-04T18:56:48.984742\",\"status\":1,\"count\":0},\"chatQuestionDto\":{\"id\":4,\"content\":\"질문내용\",\"created_at\":\"2025-03-04T18:56:49.16922\"},\"chatAnswerDto\":{\"id\":394,\"file_name\":\"HC-A-06137307000394\",\"intro\":\"에이즈는~~\",\"body\":\"HIV는~~\",\"conclusion\":\"HIV감염을~~~\"},\"is_matched\":0}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-조회 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"유효하지않은토큰입니다\"}}"
				)
			) 
		)
	})
	@GetMapping("/qna")
	public ResponseEntity<?> getQna(
			@Parameter(description = "회원의 토큰값") @RequestHeader(name = "Authorization") String token,
			@Parameter(description = "세션 아이디") @RequestParam Long sid) {
		//<찾은 회원이 존재하지 않는 경우>
		if(Commons.findMemberByToken(token, memberService) == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail("유효하지 않은 토큰입니다"));
		}
		//<찾은 회원이 존재하는 경우>
		//세션id로 qna 조회
		List<ChatQnaDto> findedQna = chatQnaService.findAllBySession(sid);
		
		for(ChatQnaDto dto: findedQna) {
			System.out.println("++++ dto:"+dto.getId());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(findedQna));
	}
	
}
