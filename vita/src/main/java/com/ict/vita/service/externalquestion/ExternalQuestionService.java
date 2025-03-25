package com.ict.vita.service.externalquestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	//모든 외부질문 조회
	public List<ExternalQuestionDto> getAll(){
		List <ExternalQuestionEntity> list = externalQuestionRepository.findAll();
		return list.stream().map(entity -> ExternalQuestionDto.toDto(entity)).toList();
	}

	//나이+성별 별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionResponseDto> getTopQuestionsByAgeAndGender(String age, char gender) {
		age = toAgeGroup(age);	
		List <Object> result = externalQuestionRepository.findTopQuestionsByAgeAndGenderGroup(age, gender);
		
		List<ExternalQuestionResponseDto> list = new Vector<>();
		for(Object obj : result) {
			Object[] row = (Object[]) obj; 
			ExternalQuestionResponseDto respDto = ExternalQuestionResponseDto.builder()
												.age_group(String.valueOf(row[0])) //age_group
												.gender((char)row[1])
												.category(String.valueOf(row[2])) //category
												.question_count(Integer.valueOf(String.valueOf(row[3]))) //question_count
												.build();
			list.add(respDto);
		}
		
		return list;
	}

	//나이별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionResponseDto> getTopQuestionsByAge(String age) {
		age = toAgeGroup(age);

		List <Object> result = externalQuestionRepository.findTopQuestionsByAgeGroup(age);
		
		/*
		List<Map<String, Object>> response = new Vector<>();
		    for (Object r : result) {
		        Object[] row = (Object[]) r; // Object[]로 캐스팅
		        Map<String, Object> map = new HashMap<>();
		        
		        System.out.println( row[0] );
		        System.out.println( row[1] );
		        System.out.println( row[2] );
		        map.put("age_group", row[0]);     // age_group
		        map.put("category", row[1]);      // category
		        map.put("question_count", row[2]); // question_count
		        response.add(map);
		 } */
		
		List<ExternalQuestionResponseDto> list = new Vector<>();
		
		for(Object obj : result) {
			Object[] row = (Object[]) obj; 
			ExternalQuestionResponseDto respDto = ExternalQuestionResponseDto.builder()
												.age_group(String.valueOf(row[0])) //age_group
												.category(String.valueOf(row[1])) //category
												.question_count(Integer.valueOf(String.valueOf(row[2]))) //question_count
												.build();
			list.add(respDto);
		}
		
		return list;
		
	}

	//성별별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionResponseDto> getTopQuestionsByGender(char gender) {
		List <Object> result = externalQuestionRepository.findTopQuestionsByGenderGroup(gender);
		
		List<ExternalQuestionResponseDto> list = new Vector<>();
		for(Object obj : result) {
			Object[] row = (Object[])obj;
			ExternalQuestionResponseDto respDto = ExternalQuestionResponseDto.builder()
					.gender((char) (row[0])) //gender
					.category(String.valueOf(row[1])) //category
					.question_count(Integer.valueOf(String.valueOf(row[2]))) //question_count
					.build();
			list.add(respDto);
		}
		
		return list;
	}

	//직업별 가장 많이 질문한 외부질문 조회
	public List<ExternalQuestionResponseDto> getTopQuestionsByOccupation(String occupation) {
		List <Object> result = externalQuestionRepository.findTopQuestionsByOccupationGroup(occupation);
		
		List<ExternalQuestionResponseDto> list = new Vector<>();
		for(Object obj : result) {
			Object[] row = (Object[])obj;
			ExternalQuestionResponseDto respDto = ExternalQuestionResponseDto.builder()
					.occupation(String.valueOf((row[0]))) //occupation
					.category(String.valueOf(row[1])) //category
					.question_count(Integer.valueOf(String.valueOf(row[2]))) //question_count
					.build();
			list.add(respDto);
		}
		
		return list;
	}
	
	
}
