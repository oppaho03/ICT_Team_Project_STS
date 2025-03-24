package com.ict.vita.service.externalquestion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.externalquestion.ExternalQuestionEntity;
import com.ict.vita.repository.externalquestion.ExternalQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ExternalQuestionService {
	//리포지토리 주입
	private final ExternalQuestionRepository externalQuestionRepository;
	
	//age를 연령대로 그룹핑
	public String toAgeGroup(String age) {
		if(age.contains("10")) age = "10대";
		else if(age.contains("20")) age = "20대";
		else if(age.contains("30")) age = "30대";
		else if(age.contains("40")) age = "40대";
		else if(age.contains("50")) age = "50대";
		else if(age.contains("60")) age = "60대";
		else age = "기타";
		
		return age;
	}

	//나이+성별 별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionDto> getTopQuestionsByAgeAndGender(String age, char gender) {
		age = toAgeGroup(age);	
		List <ExternalQuestionEntity> result = externalQuestionRepository.findTopQuestionsByAgeAndGenderGroup(age, gender);
		
		return result.stream().map(entity -> ExternalQuestionDto.toDto(entity)).toList();
	}

	//나이별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionDto> getTopQuestionsByAge(String age) {
		age = toAgeGroup(age);
		System.out.println("나이 변환:"+age);
		List <ExternalQuestionEntity> result = externalQuestionRepository.findTopQuestionsByAgeGroup(age);
		
		return result.stream().map(entity -> ExternalQuestionDto.toDto(entity)).toList();
	}

	//성별별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionDto> getTopQuestionsByGender(char gender) {
		List <ExternalQuestionEntity> result = externalQuestionRepository.findTopQuestionsByGenderGroup(gender);
		return result.stream().map(entity -> ExternalQuestionDto.toDto(entity)).toList();
	}

	//직업별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionDto> getTopQuestionsByOccupation(String occupation) {
		List <ExternalQuestionEntity> result = externalQuestionRepository.findTopQuestionsByOccupationGroup(occupation);
		return result.stream().map(entity -> ExternalQuestionDto.toDto(entity)).toList();
	}
	
	
}
