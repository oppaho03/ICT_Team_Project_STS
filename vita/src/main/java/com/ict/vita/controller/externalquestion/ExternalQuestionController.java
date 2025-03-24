package com.ict.vita.controller.externalquestion;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.chatsession.ChatSessionService;
import com.ict.vita.service.externalquestion.ExternalQuestionDto;
import com.ict.vita.service.externalquestion.ExternalQuestionService;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external-questions")
@CrossOrigin
public class ExternalQuestionController {
	//서비스 주입
	private final ExternalQuestionService externalQuestionService;
	
	/**
	 * [외부질문 통계] 
	 * 1.나이 2.성별 3.나이+성별 4.직업  별 가장 많이 질문한 외부질문 조회
	 * @param age 나이대
	 * @param gender 성별
	 * @param occupation 직업
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> getTopQuestionsByGroup(
			@RequestParam("age") String age,
			@RequestParam("gender") char gender,
			@RequestParam("occupation") String occupation){
		
		List<ExternalQuestionDto> result = null;
		
		//나이+성별 별 조회
		if( !Commons.isNull(age.trim()) && !Commons.isNull(String.valueOf(gender)) ) {
			result = externalQuestionService.getTopQuestionsByAgeAndGender(age.trim(), gender);
		}
		//나이별 조회
		else if(!Commons.isNull(age.trim())) {
			result = externalQuestionService.getTopQuestionsByAge(age);
		}
		//성별별 조회
		else if(!Commons.isNull(String.valueOf(gender))) {
			result = externalQuestionService.getTopQuestionsByGender(gender);
		}
		//직업별 조회
		else if(!Commons.isNull(occupation)) {
			result = externalQuestionService.getTopQuestionsByOccupation(occupation);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(result));
	}
	
}
