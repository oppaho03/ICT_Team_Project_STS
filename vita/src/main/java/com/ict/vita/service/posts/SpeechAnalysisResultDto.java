package com.ict.vita.service.posts;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[음성 감정 분석 결과 반환 DTO]
public class SpeechAnalysisResultDto {
	/* 예시
	   {
	    'post_id' : 1, 
	    'file_name' : 'test.wav', 
	    'transcribed_text' : '엄마 아빠도 계속 슬퍼하지', 
	    'overall_sentiment' : 'POSITIVE', 
	    'overall_score' : 0.7520623803138733, 
	    'keyword_sentiment' : {}
	   }
	*/
	private Long post_id;
	private String file_name;
	private String transcribed_text;
	private String overall_sentiment;
	private float overall_score;
	private Map<String, Object> keyword_sentiment;
}
